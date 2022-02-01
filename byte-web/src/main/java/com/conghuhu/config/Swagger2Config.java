package com.conghuhu.config;


import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author conghuhu
 * @create 2021-09-25 14:14
 * swagger2的配置类
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    /**
     * api接口包扫描路径
     */
    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.conghuhu.controller";

    public static final String VERSION = "1.0.0";

    /**
     * 读取application.yml配置文件的值
     */
    @Value("${enable.swagger}")
    private boolean enableSwagger;

    @Bean
    public Docket createRestApi() {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new ParameterBuilder()
                .name("token")
                .description("认证token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enableSwagger)
                .apiInfo(apiInfo())
                .globalOperationParameters(parameters)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build();
//                .securitySchemes(security());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ByteDance-ToDo接口文档") //设置文档的标题
                .description("后端接口文档\n " +
                        "baseUrl: http://101.201.143.127:8090/api \n" +
                        "") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .termsOfServiceUrl("http://www.baidu.com") // 设置文档的License信息->1.3 License information
                .build();
    }
}
