package com.chunlei.mall.service.Impl;

import com.chunlei.mall.bo.AdminUserDetails;
import com.chunlei.mall.common.exception.ApiException;
import com.chunlei.mall.dao.UmsAdminRoleRelationDao;
import com.chunlei.mall.mapper.UmsAdminMapper;
import com.chunlei.mall.model.UmsAdmin;
import com.chunlei.mall.model.UmsAdminExample;
import com.chunlei.mall.model.UmsResource;
import com.chunlei.mall.model.UmsRole;
import com.chunlei.mall.security.util.JwtTokenUtil;
import com.chunlei.mall.service.UmsAdminService;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UmsAdminServiceImpl.class);
    @Autowired
    private UmsAdminMapper umsAdminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UmsAdminRoleRelationDao umsAdminRoleRelationDao;

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = umsAdminMapper.selectByExample(example);
        if(adminList != null && adminList.size()>0){
            UmsAdmin admin = adminList.get(0);
            return admin;
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        //获取用户信息
        UmsAdmin admin = getAdminByUsername(username);
        if(admin != null){
            //获取权限 TODO
            return new AdminUserDetails(admin);
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        UserDetails userDetails = loadUserByUsername(username);
        try {
            if(!passwordEncoder.matches(password,userDetails.getPassword())){
                throw new ApiException("密码不正确");
            }
            if(!userDetails.isEnabled()){
                throw new ApiException("账号已被禁用");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
            // insertLoginLog() TODO
        } catch (AuthenticationException e) {
            LOGGER.warn("登录异常：{}",e.getMessage());
        }
        return token;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return umsAdminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public List<UmsAdmin> getAdminList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        UmsAdminExample umsAdminExample = new UmsAdminExample();
        return umsAdminMapper.selectByExample(umsAdminExample);
    }
}
