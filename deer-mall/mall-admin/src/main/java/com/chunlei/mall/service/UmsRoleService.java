package com.chunlei.mall.service;

import com.chunlei.mall.model.UmsMenu;
import com.chunlei.mall.model.UmsRole;

import javax.management.relation.Role;
import java.util.List;

public interface UmsRoleService {
    List<UmsMenu> getMenuList(Long uerId);

    List<UmsRole> listAll();

    List<UmsRole> getRoleList(String keyword, int pageNum, int pageSize);
}
