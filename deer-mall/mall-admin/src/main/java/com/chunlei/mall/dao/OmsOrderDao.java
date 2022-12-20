package com.chunlei.mall.dao;

import com.chunlei.mall.dto.OmsOrderDeliveryParam;
import com.chunlei.mall.dto.OmsOrderDetail;
import com.chunlei.mall.dto.OmsOrderQueryParam;
import com.chunlei.mall.model.OmsOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmsOrderDao {
    List<OmsOrder> list(@Param("queryParam") OmsOrderQueryParam queryParam);
    OmsOrderDetail detail(Long id);
    int delivery(@Param("list")List<OmsOrderDeliveryParam> deliveryParamList);
}
