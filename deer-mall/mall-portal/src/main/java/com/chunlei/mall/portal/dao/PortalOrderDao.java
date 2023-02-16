package com.chunlei.mall.portal.dao;

import com.chunlei.mall.model.OmsOrderItem;
import com.chunlei.mall.portal.domain.OmsOrderDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PortalOrderDao {

    OmsOrderDetail getDetail( Long orderId);
    int updateSkuStock(@Param("itemList")List<OmsOrderItem> orderItemList);
    int releaseSkuStockLock(@Param("itemList") List<OmsOrderItem> orderItemList);
}
