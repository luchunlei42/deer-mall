package com.chunlei.mall.service;

import com.chunlei.mall.dto.OmsOrderDeliveryParam;
import com.chunlei.mall.dto.OmsOrderDetail;
import com.chunlei.mall.dto.OmsOrderQueryParam;
import com.chunlei.mall.dto.OmsReceiverInfoParam;
import com.chunlei.mall.model.OmsOrder;

import java.util.List;

public interface OmsOrderService {
    List<OmsOrder> list(OmsOrderQueryParam queryParam, int pageNum, int pageSize);
    OmsOrderDetail detail(Long id);
    int deliver(List<OmsOrderDeliveryParam> orderDeliveryParamList);
    int close(List<Long> ids, String note);
    int delete(List<Long> ids);
    int updateReceiver(OmsReceiverInfoParam receiverInfoParam);
}
