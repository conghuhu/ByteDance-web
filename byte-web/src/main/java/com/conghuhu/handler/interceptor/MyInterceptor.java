package com.conghuhu.handler.interceptor;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.conghuhu.entity.ProUser;
import com.conghuhu.entity.User;
import com.conghuhu.mapper.ProUserMapper;
import com.conghuhu.result.JsonResult;
import com.conghuhu.result.ResultCode;
import com.conghuhu.result.ResultTool;
import com.conghuhu.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author conghuhu
 * @create 2022-01-30 20:17
 */
@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {

    private final ProUserMapper proUserMapper;

    public MyInterceptor(ProUserMapper proUserMapper) {
        this.proUserMapper = proUserMapper;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String methodName = method.getName();
        log.info("====拦截到了方法：{}，在该方法执行之前执行====", methodName);

        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/getProductShowInfo/");
        String productIdStr = split[split.length - 1];
        Long productId;

        // 设置返回值格式
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");

        try {
            productId = Long.valueOf(productIdStr);
        } catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            // productId格式化有误
            JsonResult result = ResultTool.fail(ResultCode.PRODUCT_PARAM_ERROR);
            response.getWriter().write(JSON.toJSONString(result));
            return false;
        }

        User user = UserThreadLocal.get();
        log.info("线程变量获取到：" + user.getFullname() + " " + user.getUsername());
        Integer selectCount = proUserMapper.selectCount(new LambdaQueryWrapper<ProUser>()
                .eq(ProUser::getProductId, productId)
                .eq(ProUser::getUserId, user.getUserId()));
        if (selectCount > 0) {
            return true;
        }
        JsonResult result = ResultTool.fail(ResultCode.PRODUCT_NOT_PERMISSION);
        response.getWriter().write(JSON.toJSONString(result));
        // 返回true才会继续执行，返回false则取消当前请求
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("执行完方法之后进执行(Controller方法调用之后)，但是此时还没进行视图渲染");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("整个请求都处理完，DispatcherServlet也渲染了对应的视图，清理线程变量");
        UserThreadLocal.remove();
    }

}
