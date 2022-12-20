package com.chunlei.mall.dto;

import com.chunlei.mall.model.OmsOrder;
import com.chunlei.mall.model.OmsOrderItem;
import com.chunlei.mall.model.OmsOrderOperateHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OmsOrderDetail extends OmsOrder {
    @ApiModelProperty("订单商品详情")
    List<OmsOrderItem> orderItemList;
    @ApiModelProperty("订单操作记录列表")
    List<OmsOrderOperateHistory> historyList;
}
