package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.service.UmsResourceCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("UmsResourceCategoryController")
@Tag(name = "UmsResourceCategoryController",description = "后台资源类型管理")
@RestController
@RequestMapping("/resourceCategory")
public class UmsResourceCategoryController {
    @Autowired
    UmsResourceCategoryService resourceCategoryService;

    @ApiOperation("获取所有资源列表")
    @GetMapping("/listAll")
    public CommonResult listAll(){
        return CommonResult.success(resourceCategoryService.lisAll());
    }
}
