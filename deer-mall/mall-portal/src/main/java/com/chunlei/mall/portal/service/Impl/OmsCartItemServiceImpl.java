package com.chunlei.mall.portal.service.Impl;

import cn.hutool.core.collection.CollUtil;
import com.chunlei.mall.mapper.OmsCartItemMapper;
import com.chunlei.mall.model.OmsCartItem;
import com.chunlei.mall.model.OmsCartItemExample;
import com.chunlei.mall.model.UmsMember;
import com.chunlei.mall.portal.domain.CartPromotionItem;
import com.chunlei.mall.portal.service.OmsCartItemService;
import com.chunlei.mall.portal.service.OmsPromotionService;
import com.chunlei.mall.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {
    @Autowired
    private OmsCartItemMapper cartItemMapper;
    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private OmsPromotionService promotionService;
    @Override
    public int add(OmsCartItem cartItem) {
        int count;
        UmsMember currentMember = memberService.getCurrentMember();
        cartItem.setMemberId(currentMember.getId());
        cartItem.setMemberNickname(currentMember.getNickname());
        cartItem.setDeleteStatus(0);
        OmsCartItem exitsCartItem = getCartItem(cartItem);
        if(exitsCartItem == null){
            cartItem.setCreateDate(new Date());
            count = cartItemMapper.insert(cartItem);
        }else{
            cartItem.setModifyDate(new Date());
            exitsCartItem.setQuantity(exitsCartItem.getQuantity()+ cartItem.getQuantity());
            count = cartItemMapper.updateByPrimaryKey(exitsCartItem);
        }
        return count;
    }

    @Override
    public List<OmsCartItem> list(Long memberId) {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andMemberIdEqualTo(memberId);
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId, List<Long> cartIds) {
        List<OmsCartItem> cartItemList = list(memberId);
        if(CollUtil.isNotEmpty(cartIds)){
            cartItemList = cartItemList.stream().filter(item -> cartIds.contains(item.getId())).collect(Collectors.toList());
        }
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(cartItemList)){
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public int delete(Long member, List<Long> ids) {
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids).andMemberIdEqualTo(member);
        return cartItemMapper.updateByExampleSelective(record,example);
    }


    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItemExample example = new OmsCartItemExample();
        OmsCartItemExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(cartItem.getMemberId())
                .andProductIdEqualTo(cartItem.getProductId()).andDeleteStatusEqualTo(0);
        if(cartItem.getProductSkuId() != null){
            criteria.andProductSkuIdEqualTo(cartItem.getProductSkuId());
        }
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(cartItemList)){
            return cartItemList.get(0);
        }
        return null;
    }
}
