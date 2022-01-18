package com.conghuhu.auth;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author conghuhu
 * @create 2021-10-11 20:14
 */

@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

    private final StringRedisTemplate stringRedisTemplate;

    public UserLogoutSuccessHandler(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = request.getHeader("token");
        JsonResult result = null;
        if (StringUtils.isNotBlank(token)) {
            stringRedisTemplate.delete("Token_" + token);
            result = ResultTool.success();
        } else {
            result = ResultTool.fail();
        }
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
