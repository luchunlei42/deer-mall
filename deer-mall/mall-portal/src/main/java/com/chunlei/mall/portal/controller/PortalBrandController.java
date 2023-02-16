package com.chunlei.mall.portal.controller;

import com.chunlei.mall.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "PortalBrandController")
@Tag(name = "PortalBrandController",description = "前台品牌管理")
@RequestMapping("/brand")
public class PortalBrandController {

    @ApiOperation("分页获取推荐品牌")
    @GetMapping("recommendList")
    public CommonResult recommendList(@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum){
        return CommonResult.success("Hello World");
    }
}
