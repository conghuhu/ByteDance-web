package com.conghuhu.auth;

import com.alibaba.fastjson.JSON;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultTool;
import com.conghuhu.utils.JwtTokenUtil;
import com.conghuhu.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author conghuhu
 * @create 2021-10-11 16:19
 */
@Slf4j
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private final RedisUtil redisUtil;

    private final JwtTokenUtil jwtUtils;

    public MyAuthenticationSuccessHandler(RedisUtil redisUtil, JwtTokenUtil jwtUtils) {
        this.redisUtil = redisUtil;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, String[]> loginMap = request.getParameterMap();
        String username = loginMap.get("username")[0];
        String password = loginMap.get("password")[0];

        UserDetail jwtUser = (UserDetail) authentication.getPrincipal();

        String token = jwtUtils.createToken(jwtUser.getUsername(), password);

        log.info("存储到redis的信息" + JSON.toJSONString(jwtUser));

        stringRedisTemplate.opsForValue().set("Token_" + token, JSON.toJSONString(jwtUser.getUser()), 1, TimeUnit.DAYS);

        HashMap<Object, Object> map = new HashMap<>(2);
        map.put("token", token);
        map.put("username", jwtUser.getUsername());
        //返回json数据
        JsonResult result = ResultTool.success(map);
        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");
        //塞到HttpServletResponse中返回给前台
        response.getWriter().write(JSON.toJSONString(result));
    }
}
