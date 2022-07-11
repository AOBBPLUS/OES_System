package com.oes.servicebase.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Swagger的配置类
@Configuration // 配置类注解
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket weApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
        //不显示admin和error目录
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("在线教育系统API文档")
                .description("本文档描述类课程中心微服务接口定义")
                .version("1.0")
                .contact(new Contact("nichijoux", "https://github.com/nichijoux", "1647365387@qq.com"))
                .build();
    }
}
