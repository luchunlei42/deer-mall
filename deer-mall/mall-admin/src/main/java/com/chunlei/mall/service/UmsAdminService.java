package com.chunlei.mall.service;

import com.chunlei.mall.model.UmsAdmin;
import org.springframework.security.core.userdetails.UserDetails;

public interface UmsAdminService {
    UmsAdmin getAdminByUsername(String username);

    UserDetails loadUserByUsername(String username);
}
