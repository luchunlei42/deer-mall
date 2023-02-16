package com.chunlei.mall.portal.dao;

import com.chunlei.mall.portal.domain.CartProduct;
import com.chunlei.mall.portal.domain.PromotionProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PortalProductDao {
    CartProduct getCartProduct(Long id);

    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);
}
