package com.chunlei.mall.util;

import com.chunlei.mall.model.DBTypeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicDbUtil {
    private static final ThreadLocal<DBTypeEnum> CONTEXT_HAND = new ThreadLocal<>();

    public static void set(DBTypeEnum dbTypeEnum){
        CONTEXT_HAND.set(dbTypeEnum);
        log.info("切换数据源{}",dbTypeEnum);
    }
    public static void master(){
        set(DBTypeEnum.MASTER);
    }
    public static void slave(){
        set(DBTypeEnum.SLAVE);
    }
    public static void remove(){
        CONTEXT_HAND.remove();
    }
    public static DBTypeEnum get(){
        return CONTEXT_HAND.get();
    }
}
