package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.service.CmsSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "CmsSubjectController")
@Tag(name = "CmsSubjectController",description = "商品专题管理")
@RequestMapping("/subject")
public class CmsSubjectController {
    @Autowired
    CmsSubjectService cmsSubjectService;

    @ApiOperation("获取全部商品专题")
    @GetMapping("/listAll")
    public CommonResult listAll(){
        return CommonResult.success(cmsSubjectService.listAll());
    }

}
