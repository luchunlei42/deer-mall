package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.dto.PmsProductCategoryParam;
import com.chunlei.mall.dto.PmsProductParam;
import com.chunlei.mall.dto.PmsProductQueryParam;
import com.chunlei.mall.model.PmsProduct;
import com.chunlei.mall.service.PmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "PmsProductController")
@Tag(name = "PmsProductControlle", description = "后台产品管理")
@RestController
@RequestMapping("/product")
public class PmsProductController {

    @Autowired
    PmsProductService pmsProductService;

    @ApiOperation("后台产品分页列表")
    @GetMapping("/list")
    public CommonResult getProductList(PmsProductQueryParam pmsProductQueryParam, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize){
        List<PmsProduct> productList = pmsProductService.getProductList(pmsProductQueryParam, pageNum,pageSize);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @ApiOperation("后台新增商品")
    @PostMapping("/create")
    public CommonResult create(@RequestBody PmsProductParam productParam){
        int count = pmsProductService.create(productParam);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

    @ApiOperation("更新商品")
    @PostMapping("/update/{id}")
    public CommonResult update(@RequestBody PmsProductParam productParam, @PathVariable Long id){
        int count = pmsProductService.update(id, productParam);
        if(count > 0){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }

}
