package com.chunlei.mall.aspect;

import com.chunlei.mall.util.DynamicDbUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class DataSourceAop {

    @Pointcut("@annotation(com.chunlei.mall.annotation.ReadOnly)")
    public void readPointCut(){}

    @Before("readPointCut()")
    public void readAdvice(){
        log.info("切换数据源为从数据库");
        DynamicDbUtil.slave();
    }
}
