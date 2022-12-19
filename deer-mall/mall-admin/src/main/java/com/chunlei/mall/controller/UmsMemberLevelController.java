package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.service.UmsMemberLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@Api(tags = "UmsMemberLevelController")
@Tag(name = "UmsMemberLevelController", description = "会员等级接口")
@RestController
@RequestMapping("/memberLevel")
public class UmsMemberLevelController {
    @Autowired
    UmsMemberLevelService umsMemberLevelService;

    @ApiOperation("获取会员等级列表")
    @GetMapping("/list")
    public CommonResult getList(@RequestParam(value = "defaultStatus") int defaultStatus){
        return CommonResult.success(umsMemberLevelService.getList(defaultStatus));
    }
}
