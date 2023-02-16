package com.chunlei.mall.portal.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.portal.domain.ConfirmOrderResult;
import com.chunlei.mall.portal.domain.OmsOrderDetail;
import com.chunlei.mall.portal.domain.OrderParam;
import com.chunlei.mall.portal.service.OmsPortalOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@Api(tags = "OmsPortalOrderController")
@Tag(name = "OmsPortalOrderController", description = "订单管理")
@RequestMapping("/order")
public class OmsPortalOrderController {

    @Autowired
    OmsPortalOrderService portalOrderService;

    @ApiOperation("根据购物车信息生成确认单")
    @PostMapping("/generateConfirmOrder")
    public CommonResult generateConfirmOrder(@RequestBody List<Long> cardIds){
        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder(cardIds);
        return CommonResult.success(confirmOrderResult);
    }

    @ApiOperation("根据购物车信息生成订单")
    @PostMapping("/generateOrder")
    public CommonResult generateOrder(@RequestBody OrderParam orderParam){
        Map<String,Object> result = portalOrderService.generateOrder(orderParam);
        return  CommonResult.success(result,"下单成功");
    }

    @ApiOperation("用户支付成功的回调")
    @PostMapping("/paySuccess")
    public CommonResult paySuccess(@RequestParam Long orderId, @RequestParam Integer payType){
        Integer count = portalOrderService.paySuccess(orderId, payType);
        return CommonResult.success(count, "支付成功");
    }
    @ApiOperation("按状态分页获取用户订单列表")
    @ApiImplicitParam(name = "status", value = "订单状态：-1->全部；0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
            defaultValue = "-1", allowableValues = "-1,0,1,2,3,4", paramType = "query", dataType = "int")
    @GetMapping("/list")
    public CommonResult list(@RequestParam Integer status,
                             @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(required = false, defaultValue = "5") Integer pageSize){
        CommonPage<OmsOrderDetail> orderPage = portalOrderService.list(status,pageNum,pageSize);
        return CommonResult.success(orderPage);
    }
}
