package com.conghuhu.controller;


import com.conghuhu.entity.User;
import com.conghuhu.params.MailParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.MailService;
import com.conghuhu.service.UserService;
import com.conghuhu.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-28
 */
@Api(tags = "邮件类(开发中)")
@RestController
@RequestMapping("/mail")
public class MailController {

    private static final String REGISTER = "register";
    private static final String RESET = "reset";
    private static final Long EXTRA_EXPIRE = 175L;

    private final RedisUtil redisUtil;
    private final MailService mailService;

    private final UserService userService;

    public MailController(MailService mailService, RedisUtil redisUtil, UserService userService) {
        this.mailService = mailService;
        this.redisUtil = redisUtil;
        this.userService = userService;
    }

    @PostMapping("/send")
    public JsonResult sendMail(@RequestBody MailParam mailParam) {
        return mailService.sendMail(mailParam);
    }

    @ApiOperation(value = "向邮件中发送验证码(无需token)", notes = "actionType为register/reset \n 无需token", produces = "application/json")
    @PostMapping("/sendVerifyCodeToMail")
    public JsonResult sendVerifyCodeToMail(@RequestParam String email, @RequestParam String actionType) {
        if (REGISTER.equals(actionType)) {
            User user = userService.getByUserName(email);
            if (user != null) {
                return ResultTool.fail(ResultCode.ACCOUNT_EXIST);
            }
        }
        long expire = redisUtil.getExpire(email + "_code");
        if (expire <= EXTRA_EXPIRE) {
            MailParam mailParam = new MailParam();
            Map<String, Object> model = new HashMap<>(2);
            if (REGISTER.equals(actionType)) {
                model.put("actionType", "注册账号");
            } else if (RESET.equals(actionType)) {
                model.put("actionType", "重置密码");
            } else {
                return ResultTool.fail(ResultCode.PARAM_TYPE_ERROR);
            }
            String randCode = getRandCode(1, 999999);
            redisUtil.set(email + "_code", randCode);
            redisUtil.setExpire(email + "_code", 240);
            model.put("verifyCode", randCode);
            mailParam.setSubject("ToDo团队");
            mailParam.setTo(email);
            mailParam.setTemplateFile("verifyCode.html");
            mailParam.setModel(model);
            return mailService.sendMail(mailParam);
        } else {
            return ResultTool.fail(ResultCode.MAIL_CODE_ALREADY_SEND);
        }

    }

    public static String getRandCode(int min, int max) {
        int randNum;
        do {
            randNum = min + (int) (Math.random() * ((max - min) + 1));
        } while (randNum <= 100000 || randNum > max);
        return String.valueOf(randNum);
    }
}
