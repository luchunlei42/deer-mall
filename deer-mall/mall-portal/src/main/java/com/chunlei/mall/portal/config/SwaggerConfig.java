package com.chunlei.mall.portal.config;

import com.chunlei.mall.common.config.BaseSwaggerConfig;
import com.chunlei.mall.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {
    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.chunlei.mall.portal.controller")
                .title("mall前台项目")
                .description("mall前台项目相关接口文档")
                .contactName("Chunlei Lu")
                .version("1.1")
                .enableSecurity(true)
                .build();
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor(){
        return generateBeanPostProcessor();
    }
}
