package com.chunlei.mall.portal.service;

import com.chunlei.mall.model.OmsCartItem;
import com.chunlei.mall.portal.domain.CartPromotionItem;

import java.util.List;

public interface OmsPromotionService {
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
