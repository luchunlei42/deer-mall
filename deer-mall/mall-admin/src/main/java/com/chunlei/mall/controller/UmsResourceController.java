package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.service.UmsResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("UmsResourceController")
@Tag(name = "UmsResourceController",description = "后台资源管理")
@RestController
@RequestMapping("/resource")
public class UmsResourceController {
    @Autowired
    UmsResourceService resourceService;

    @ApiOperation("获取资源列表")
    @GetMapping("/list")
    public CommonResult getList(@RequestParam("pageNum")int pageNum, @RequestParam("pageSize") int pageSize){
        return CommonResult.success(CommonPage.restPage(resourceService.getList(pageNum, pageSize)));
    }
}
