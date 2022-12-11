package com.chunlei.mall.common.service;

/**
 * redis操作service,
 * 对象和数组都已json形式储存
 */
public interface RedisService {

    void set(String key, String value);

    void get(String key);

    boolean expire(String key, long expire);

    void remove(String key);

    /**
     *自增操作
     * @param key
     * @param delta 自增步长
     * @return
     */
    Long increment(String key, long delta);
}
