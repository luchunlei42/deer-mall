package com.chunlei.mall.portal.dao;

import com.chunlei.mall.model.SmsCoupon;
import com.chunlei.mall.portal.domain.SmsCouponHistoryDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmsCouponHistoryDao {
    List<SmsCouponHistoryDetail> getDetailList(Long memberId);
    List<SmsCoupon> getCouponList(@Param("memberId") Long memberId, @Param("useStatus") Integer useStatus);
}
