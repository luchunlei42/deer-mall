package com.chunlei.mall.service;

import com.chunlei.mall.model.UmsAdmin;
import com.chunlei.mall.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UmsAdminService {
    UmsAdmin getAdminByUsername(String username);

    UserDetails loadUserByUsername(String username);

    String login(String username, String password);

    List<UmsRole> getRoleList(Long adminId);

    List<UmsAdmin> getAdminList(int pageNum, int pageSize);

}
