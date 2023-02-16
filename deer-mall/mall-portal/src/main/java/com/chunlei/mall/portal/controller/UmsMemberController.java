package com.chunlei.mall.portal.controller;

import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.portal.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "UmsMemberController")
@Tag(name = "UmsMemberController", description = "会员登录注册管理")
@RequestMapping("/sso")
public class UmsMemberController {

    @Value("${jwt.tokenHeader}")
    private String tokenHead;
    @Autowired
    private UmsMemberService memberService;

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public CommonResult login(@RequestParam String username,@RequestParam String password){
        String token = memberService.login(username, password);
        if(token == null){
            return CommonResult.failed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }
}
