package com.chunlei.mall.portal.service;

import com.chunlei.mall.model.OmsCartItem;
import com.chunlei.mall.portal.domain.CartPromotionItem;

import java.util.List;

public interface OmsCartItemService {
    public int add(OmsCartItem cartItem);
    public List<OmsCartItem> list(Long memberId);
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds);
    public int delete(Long member, List<Long> ids);
}
