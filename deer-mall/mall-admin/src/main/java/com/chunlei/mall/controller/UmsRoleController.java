package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "UmsRoleController")
@Tag(name = "UmsRoleController", description = "后台用户角色管理")
@RequestMapping("/role")
public class UmsRoleController {
    @Autowired
    UmsRoleService roleService;

    @ApiOperation("获取所有角色列表")
    @GetMapping("/listAll")
    public CommonResult listAll(){
        return CommonResult.success(CommonPage.restPage(roleService.listAll()));
    }

    @GetMapping("/list")
    @ApiOperation("获取角色分页列表")
    public CommonResult list(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, @RequestParam(value = "keyword", required = false) String keyword){
        return CommonResult.success(CommonPage.restPage(roleService.getRoleList(keyword, pageNum, pageSize)));
    }
}
