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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        String verifyCode = registerParam.getVerifyCode();
        String fullName = registerParam.getFullName();

        User user = userService.getByUserName(username);
        if(user != null){
            return ResultTool.fail(ResultCode.ACCOUNT_EXIST);
        }else{
            User newUser = new User();
            newUser.setUsername(username);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            newUser.setPassword(passwordEncoder.encode(password));
            newUser.setCreatedTime(LocalDateTime.now());
            newUser.setLastLoginTime(LocalDateTime.now());
            newUser.setFullname(fullName);
            newUser.setAvatar("https://joeschmoe.io/api/v1/random");
            this.userService.save(newUser);
            String token = jwtUtils.createToken(username,password);
            stringRedisTemplate.opsForValue().set("Token_"+token, JSON.toJSONString(newUser),1, TimeUnit.DAYS);

            return ResultTool.success(token);
        }
    }
}
