package com.chunlei.mall.portal.service;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.portal.domain.ConfirmOrderResult;
import com.chunlei.mall.portal.domain.OmsOrderDetail;
import com.chunlei.mall.portal.domain.OrderParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface OmsPortalOrderService {
    ConfirmOrderResult generateConfirmOrder(List<Long> cartIds);

    @Transactional
    Map<String,Object> generateOrder(OrderParam orderParam);
    @Transactional
    Integer paySuccess(Long orderId, Integer payType);
    public void sendDelayMessageCancelOrder(Long orderId);
    /**
     * 取消单个超时订单
     */
    @Transactional
    void cancelOrder(Long orderId);
    @Transactional
    Integer cancelTimeOutOrder();
    CommonPage<OmsOrderDetail> list(Integer status, Integer pageNum, Integer pageSize);
}
