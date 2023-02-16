package com.chunlei.mall.portal.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.mall.mapper","com.chunlei.mall.portal.dao"})
public class MyBatisConfig {
}
