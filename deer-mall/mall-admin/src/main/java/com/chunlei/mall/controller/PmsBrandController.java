package com.chunlei.mall.controller;

import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.model.PmsBrand;
import com.chunlei.mall.service.PmsBrandService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Api(tags = "PmsBrandController")
@Tag(name = "PmsBrandController", description = "后台品牌管理")
@RestController
@RequestMapping("/brand")
public class PmsBrandController {

    @Autowired
    private PmsBrandService pmsBrandService;

    @ApiOperation("品牌分页列表")
    @GetMapping("/list")
    public CommonResult getList(@RequestParam(value = "pageNum") int pageNum, @RequestParam(value = "pageSize") int pageSize){
        List<PmsBrand> brandList = pmsBrandService.getBrandList(pageNum, pageSize);
        return CommonResult.success(CommonPage.restPage(brandList));
    }

}
