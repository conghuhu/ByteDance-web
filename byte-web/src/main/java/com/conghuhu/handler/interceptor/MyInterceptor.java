package com.conghuhu.handler.interceptor;


import com.conghuhu.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author conghuhu
 * @create 2022-01-30 20:17
 */
@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("====拦截路由requestURI：" + requestURI);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        log.info("执行完方法之后进执行(Controller方法调用之后)，但是此时还没进行视图渲染");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 整个请求都处理完，DispatcherServlet也渲染了对应的视图
        UserThreadLocal.remove();
    }

}
