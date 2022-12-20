package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.dto.OmsOrderDeliveryParam;
import com.chunlei.mall.dto.OmsOrderQueryParam;
import com.chunlei.mall.dto.OmsReceiverInfoParam;
import com.chunlei.mall.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "OmsOrderController")
@Tag(name = "OmsOrderController", description = "订单管理")
@RequestMapping("/order")
public class OmsOrderController {
    @Autowired
    OmsOrderService orderService;

    @ApiOperation("查询订单")
    @GetMapping("/list")
    public CommonResult list(OmsOrderQueryParam queryParam, @RequestParam("pageSize")int pageSize,@RequestParam("pageNum")int pageNum){
        return CommonResult.success(CommonPage.restPage(orderService.list(queryParam, pageNum, pageSize)));
    }

    @ApiOperation("获取订单详情：订单信息，商品信息，操作记录")
    @GetMapping("/{id}")
    public CommonResult detail(@PathVariable Long id){
        return CommonResult.success(orderService.detail(id));
    }

    @ApiOperation("批量发货")
    @PostMapping("/update/delivery")
    public CommonResult deliver(@RequestBody List<OmsOrderDeliveryParam> deliveryParamList){
        return CommonResult.success(orderService.deliver(deliveryParamList));
    }

    public CommonResult close(@RequestParam("ids") List<Long> ids, @RequestParam String note){
        int count = orderService.close(ids,note);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改收货人信息")
    @PostMapping("/update/receiverInfo")
    public CommonResult updateReceiverInfo(@RequestBody OmsReceiverInfoParam receiverInfoParam){
        int count = orderService.updateReceiver(receiverInfoParam);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

}
