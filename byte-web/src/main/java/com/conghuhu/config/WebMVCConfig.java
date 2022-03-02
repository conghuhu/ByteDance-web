package com.conghuhu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.conghuhu.handler.interceptor.MyInterceptor;

/**
 * @author conghuhu
 * @create 2021-10-08 11:07
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    private final MyInterceptor myInterceptor;

    public WebMVCConfig(MyInterceptor myInterceptor) {
        this.myInterceptor = myInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 跨域配置
        registry.addMapping("/**").allowedOrigins("http://localhost:8080", "http://47.103.98.136/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).addPathPatterns("/**");
    }
}
