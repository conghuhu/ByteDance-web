package com.conghuhu.auth;

/**
 * @author conghuhu
 * @create 2021-09-30 15:16
 */

import com.alibaba.fastjson.JSON;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Hutengfei
 * @Description: 匿名用户访问无权限资源时的异常
 * @Date Create in 2019/9/3 21:35
 */
@Component
public class CustomizeAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        JsonResult result = ResultTool.fail(ResultCode.USER_NOT_LOGIN);
        System.out.println(e);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}

