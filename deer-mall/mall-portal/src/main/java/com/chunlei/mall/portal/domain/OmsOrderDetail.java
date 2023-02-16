package com.chunlei.mall.portal.domain;

import com.chunlei.mall.model.OmsOrder;
import com.chunlei.mall.model.OmsOrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OmsOrderDetail extends OmsOrder {
    @ApiModelProperty("订单商品列表")
    private List<OmsOrderItem> orderItemList;
}
