package com.conghuhu.controller;


import com.conghuhu.params.MailParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.service.MailService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send")
    public JsonResult sendMail(@RequestBody MailParam mailParam) {
        return mailService.sendMail(mailParam);
    }
}
