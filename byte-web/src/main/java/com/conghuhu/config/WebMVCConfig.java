package com.conghuhu.config;

import com.conghuhu.handler.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://101.201.143.127/")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).addPathPatterns("/**");
    }
}
