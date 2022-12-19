package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.dto.PmsProductAttributeParam;
import com.chunlei.mall.service.PmsProductAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "PmsProductAttributeController")
@Tag(name = "PmsProductAttributeController",description = "后台产品属性接口")
@RestController
@RequestMapping("productAttribute")
public class PmsProductAttributeController {
    @Autowired
    PmsProductAttributeService attributeService;

    @ApiOperation("获取属性列表")
    @GetMapping("/list/{cid}")
    public CommonResult getAttrList(@PathVariable Long cid, @RequestParam("pageNum") int pageNum,@RequestParam("pageSize") int pageSize){
        return CommonResult.success(CommonPage.restPage(attributeService.getAttrList(cid,pageNum,pageSize)));
    }

    @ApiOperation("添加商品属性信息")
    @PostMapping("/create")
    public CommonResult create(@RequestBody PmsProductAttributeParam pmsProductAttributeParam){
        int count = attributeService.createAttr(pmsProductAttributeParam);
        if(count > 0){
            return CommonResult.success(count);
        }else {
            return CommonResult.failed();
        }
    }

}
