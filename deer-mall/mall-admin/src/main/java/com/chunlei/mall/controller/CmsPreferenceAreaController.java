package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.service.CmsPreferenceAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "CmsPreferenceAreaController")
@Tag(name = "CmsPreferenceAreaController",description = "商品优选管理")
@RequestMapping("/prefrenceArea")
public class CmsPreferenceAreaController {
    @Autowired
    CmsPreferenceAreaService preferenceAreaService;

    @GetMapping("/listAll")
    @ApiOperation("获取所有商品优选")
    public CommonResult listAll(){
        return CommonResult.success(preferenceAreaService.listAll());
    }

}
