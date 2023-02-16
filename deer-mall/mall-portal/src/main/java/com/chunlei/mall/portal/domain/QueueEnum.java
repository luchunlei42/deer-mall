package com.chunlei.mall.portal.domain;

import lombok.Getter;

@Getter
public enum QueueEnum {
    /**
     * 消息通知队列
     */
    QUEUE_ORDER_CANCEL("chunleo.order.direct","chunlei.order.cancel","chunlei.order.cancel"),
    /**
     * 消息通知ttl队列
     */
    QUEUE_TTL_ORDER_CANCEL("chunlei.order.direct.ttl","chunlei.order.direct.ttl","chunlei.order.direct.ttl");
    /**
     * 交换名称
     */
    private final String exchange;
    /**
     * 队列名称
     */
    private final String name;
    /**
     * 路由键
     */
    private final String routeKey;
    QueueEnum(String exchange,String name, String routeKey){
        this.exchange = exchange;
        this.name = name;
        this.routeKey = routeKey;
    }
}
