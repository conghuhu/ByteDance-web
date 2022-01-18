package com.conghuhu.auth;

import com.alibaba.fastjson.JSON;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.service.UserService;
import com.conghuhu.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author conghuhu
 * @create 2021-10-05 19:40
 */

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    public TokenAuthenticationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("=================" + request.getRequestURI());
        //不需要鉴权
        if (request.getRequestURI().contains("login")) {
            chain.doFilter(request, response);
        }
        UsernamePasswordAuthenticationToken authentication  = null;
        JsonResult result = null;

        //处理编码方式，防止中文乱码的情况
        response.setContentType("text/json;charset=utf-8");

        authentication = getAuthentication(request);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            result = ResultTool.fail(ResultCode.USER_NOT_LOGIN);
            System.out.println("鉴权失败");
            response.getWriter().write(JSON.toJSONString(result));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 获取Token字符串，token 置于 header 里
        String token = request.getHeader("token");

        if (!StringUtils.hasText(token)) {
            token = request.getParameter("token");
        }
        System.out.println("token为:"+token);
        if (token != null && !"".equals(token.trim())) {
            // 从Token中解密获取用户名
            String userName = JwtTokenUtil.getUserNameFromToken(token);
            System.out.println("token解析出用户名"+userName);
            if (userName != null) {
                String nativePassword = JwtTokenUtil.getUserPasswordFromToken(token);
                return new UsernamePasswordAuthenticationToken(userName, nativePassword);
            } else {
                return null;
            }
        }
        return null;
    }
}
