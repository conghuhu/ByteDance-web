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
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author conghuhu
 * @create 2021-10-11 20:48
 */
@Service
public class RegisterServiceImpl extends ServiceImpl<UserMapper, User> implements RegisterService {

    private final UserService userService;

    private final JwtTokenUtil jwtUtils;

    private final StringRedisTemplate stringRedisTemplate;

    public RegisterServiceImpl(UserService userService, JwtTokenUtil jwtUtils, StringRedisTemplate stringRedisTemplate) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.stringRedisTemplate = stringRedisTemplate;
    }

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
        String email = registerParam.getEmail();
        String mobilePhoneNumber = registerParam.getMobilePhoneNumber();

        User user = userService.getByUserName(username);
        if(user != null){
            return ResultTool.fail(ResultCode.ACCOUNT_EXIST);
        }else{
            User newUser = new User();
            newUser.setUsername(username);
            System.out.println(password);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setCreated(LocalDateTime.now());
            newUser.setLastLogin(LocalDateTime.now());
            newUser.setAvatar("/static/img/logo.b3a48c0.png");
            newUser.setAdmin(false);
            newUser.setSalt("");
            newUser.setStatus(1);
            newUser.setEmail(email);
            newUser.setMobilePhoneNumber(mobilePhoneNumber);
            this.userService.save(newUser);
            String token = jwtUtils.createToken(username,password);
            stringRedisTemplate.opsForValue().set("Token_"+token, JSON.toJSONString(newUser),1, TimeUnit.DAYS);

            return ResultTool.success(token);
        }
    }
}
