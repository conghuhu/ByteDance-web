package com.conghuhu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.conghuhu.entity.User;
import com.conghuhu.mapper.UserMapper;
import com.conghuhu.params.RegisterParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.RegisterService;
import com.conghuhu.service.UserService;
import com.conghuhu.utils.JwtTokenUtil;
import com.conghuhu.utils.RedisUtil;
import com.conghuhu.utils.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author conghuhu
 * @create 2021-10-11 20:48
 */
@Slf4j
@Service
public class RegisterServiceImpl extends ServiceImpl<UserMapper, User> implements RegisterService {


    private final RedisUtil redisUtil;

    private final UserService userService;

    private final JwtTokenUtil jwtUtils;


    public RegisterServiceImpl(UserService userService, JwtTokenUtil jwtUtils, RedisUtil redisUtil) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.redisUtil = redisUtil;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JsonResult register(RegisterParam registerParam) {
        /**
         * 1. 判断账号是否存在
         * 2. 如果不存在，注册用户
         * 3. 生成token
         * 4. 存入redis，并返回
         * 5. 注意 加上事务，一旦中间任何过程出现问题，注册的用户 需要回滚
         */
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();
        String verifyCode = registerParam.getVerifyCode();
        String fullName = registerParam.getFullName();
        String avatar = registerParam.getAvatar();
        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(verifyCode)) {
            return ResultTool.fail(ResultCode.PARAM_IS_BLANK);
        }
        if (!ValidateUtil.validateEmail(username)) {
            return ResultTool.fail(ResultCode.PARAMS_ERROR);
        }

        User user = userService.getByUserName(username);
        if (user != null) {
            return ResultTool.fail(ResultCode.ACCOUNT_EXIST);
        }

        String realCode = (String) redisUtil.get(username + "_code");
        if (!verifyCode.equals(realCode)) {
            return ResultTool.fail(ResultCode.MAIL_CODE_ERROR);
        }

        User newUser = new User();
        newUser.setUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setCreatedTime(LocalDateTime.now());
        newUser.setLastLoginTime(LocalDateTime.now());
        newUser.setFullname(fullName);
        newUser.setAvatar("#" + avatar);
        newUser.setIsNews(true);
        userService.save(newUser);

        String token = jwtUtils.createToken(username, password);
        newUser.setToken(token);
        redisUtil.set("Token_" + username, newUser);
        redisUtil.setExpire("Token_" + username, 86400);

        return ResultTool.success(token);

    }
}
