package com.chunlei.mall.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.chunlei.mall.mapper","com.chunlei.mall.dao"})
public class MybatisConfig {

}
