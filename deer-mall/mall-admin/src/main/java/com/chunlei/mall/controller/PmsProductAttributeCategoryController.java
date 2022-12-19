package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.service.PmsProductAttributeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "PmsProductAttributeCategoryController")
@Tag(name = "PmsProductAttributeCategoryController",description = "后台产品属性类型接口")
@RestController
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {

    @Autowired
    PmsProductAttributeCategoryService pmsProductAttributeCategoryService;

    @ApiOperation("获取产品属性类型列表")
    @GetMapping("/list")
    public CommonResult productAttributeCategoryList(@RequestParam(value = "pageSize",defaultValue = "5") int pageSize,
                                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum){
        return CommonResult.success(CommonPage.restPage(pmsProductAttributeCategoryService.getProductAttributeCategoryList(pageSize, pageNum)));
    }

    @ApiOperation("获取所有产品属性类型同属性")
    @GetMapping("/list/withAttr")
    public CommonResult productAttributeCategoryListWithAttribute(){
        return CommonResult.success(pmsProductAttributeCategoryService.getProductAttributeCategoryListWithAttr());
    }


}
