package com.chunlei.mall.portal.service;

import com.chunlei.mall.model.UmsMember;
import org.springframework.security.core.userdetails.UserDetails;

public interface UmsMemberService {

    public UserDetails loadUserByUsername(String username);

    public UmsMember getByUsername(String username);

    public String login(String username, String password);

    public UmsMember getCurrentMember();
}
