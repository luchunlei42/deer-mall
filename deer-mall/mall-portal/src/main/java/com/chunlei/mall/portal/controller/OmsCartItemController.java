package com.chunlei.mall.portal.controller;

import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.model.OmsCartItem;
import com.chunlei.mall.portal.domain.CartPromotionItem;
import com.chunlei.mall.portal.service.OmsCartItemService;
import com.chunlei.mall.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "OmsCartItemController")
@Tag(name = "OmsCartItemController", description = "购物车管理")
@RequestMapping("/cart")
public class OmsCartItemController {
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("添加商品到购物车")
    @PostMapping("/add")
    public CommonResult add(@RequestBody OmsCartItem cartItem){
        int count = cartItemService.add(cartItem);
        if(count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
    @ApiOperation("获取当前会员的购物车列表")
    @GetMapping("/list")
    public CommonResult list(){
        List<OmsCartItem> cartItemList = cartItemService.list(memberService.getCurrentMember().getId());
        return CommonResult.success(cartItemList);
    }

    @ApiOperation("获取当前会员的购物车列表,包括促销信息")
    @GetMapping("/list/promotion")
    public CommonResult listPromotion(@RequestParam(required = true) List<Long> cardIds){
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId(),cardIds);
        return CommonResult.success(cartPromotionItemList);
    }

    @ApiOperation("删除购物车中的指定商品")
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids){
        int count = cartItemService.delete(memberService.getCurrentMember().getId(),ids);
        if (count > 0){
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
