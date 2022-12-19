package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.service.UmsMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("UmsMenuController")
@Tag(name = "UmsMenuController",description = "后台菜单接口")
@RestController
@RequestMapping("/menu")
public class UmsMenuController {
    @Autowired
    UmsMenuService menuService;

    @ApiOperation("获取菜单列表")
    @GetMapping("/list/{parent_id}")
    public CommonResult getMenuList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize")int pageSize,@PathVariable Long parent_id){
        return CommonResult.success(CommonPage.restPage(menuService.getMenuList(parent_id, pageNum, pageSize)));
    }

}
