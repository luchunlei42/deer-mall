package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.dao.PmsProductCategoryDao;
import com.chunlei.mall.dto.PmsProductCategoryParam;
import com.chunlei.mall.dto.PmsProductCategoryWithChildrenItem;
import com.chunlei.mall.model.PmsProductCategory;
import com.chunlei.mall.service.PmsProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "PmsProductCategoryController")
@Tag(name = "PmsProductCategoryController", description = "后台商品分类接口")
@RestController
@RequestMapping("productCategory")
public class PmsProductCategoryController {

    @Autowired
    PmsProductCategoryService pmsProductCategoryService;

    @ApiOperation("获取所有分类包括子类")
    @GetMapping("/list/withChildren")
    public CommonResult listwithChindren(){
        List<PmsProductCategoryWithChildrenItem> list = pmsProductCategoryService.getListWithChildren();
        return CommonResult.success(list);
    }

    @ApiOperation("获取分类分页列表")
    @GetMapping("/list/{parent_id}")
    public CommonResult getList(@PathVariable Long parent_id,
                                @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize",defaultValue = "5" )int pageSize){
        return CommonResult.success(CommonPage.restPage(pmsProductCategoryService.getList(parent_id,pageSize,pageNum)));
    }

    @PostMapping("/create")
    @ApiOperation("后台创建产品分类")
    public CommonResult create(@RequestBody PmsProductCategoryParam pmsProductCategoryParam){
        int count = pmsProductCategoryService.create(pmsProductCategoryParam);
        if(count > 0 ){
            return CommonResult.success(count);
        }else{
            return CommonResult.failed();
        }
    }
}
