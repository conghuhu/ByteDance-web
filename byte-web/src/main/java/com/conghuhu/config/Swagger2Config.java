package com.conghuhu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author conghuhu
 * @create 2021-09-25 14:14
 * swagger2的配置类
 */
@Configuration
@EnableSwagger2 // 激活swagger2
public class Swagger2Config {

    //api接口包扫描路径
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.conghuhu.controller";

    public static final String VERSION = "1.0.0";

    @Value("${enable.swagger}") // 读取application.yml配置文件的值
    private boolean enableSwagger;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enableSwagger)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("我的博客文档") //设置文档的标题
                .description("博客 API 接口文档") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .termsOfServiceUrl("http://www.baidu.com") // 设置文档的License信息->1.3 License information
                .build();
    }
}
