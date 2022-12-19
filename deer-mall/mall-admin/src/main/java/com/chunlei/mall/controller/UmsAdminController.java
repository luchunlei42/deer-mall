package com.chunlei.mall.controller;

import cn.hutool.core.collection.CollUtil;
import com.chunlei.mall.common.api.CommonPage;
import com.chunlei.mall.common.api.CommonResult;
import com.chunlei.mall.dto.UmsAdminLoginParam;
import com.chunlei.mall.model.UmsAdmin;
import com.chunlei.mall.model.UmsRole;
import com.chunlei.mall.service.Impl.UmsAdminServiceImpl;
import com.chunlei.mall.service.UmsAdminService;
import com.chunlei.mall.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(tags = "UmsAdminController")
@Tag(name = "UmsAdminController", description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminController.class);
    @Autowired
    private UmsAdminService umsAdminService;
    @Autowired
    private UmsRoleService umsRoleService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @ApiOperation(value = "后台用户登录")
    @PostMapping("/login")
    @ResponseBody
    public CommonResult login(@RequestBody UmsAdminLoginParam umsAdminLoginParam){
        String token = umsAdminService.login(umsAdminLoginParam.getUsername(),umsAdminLoginParam.getPassword());
        if(token == null){
            return CommonResult.validateFailed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        //LOGGER.info("this token:{}",tokenMap.toString());
        return CommonResult.success(tokenMap);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping("/info")
    @ResponseBody
    public CommonResult getAdmininfo(Principal principal){
        if(principal == null){
            return CommonResult.unauthorized(null);
        }
        UmsAdmin umsAdmin = umsAdminService.getAdminByUsername(principal.getName());
        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("menus", umsRoleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = umsAdminService.getRoleList(umsAdmin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles", roles);
        }
        return  CommonResult.success(data);
    }

    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public CommonResult getAdminList(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize){
        return CommonResult.success(CommonPage.restPage(umsAdminService.getAdminList(pageNum, pageSize)));
    }
}
