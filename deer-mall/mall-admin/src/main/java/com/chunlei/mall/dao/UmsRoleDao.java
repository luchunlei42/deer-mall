package com.chunlei.mall.dao;

import com.chunlei.mall.model.UmsMenu;

import java.util.List;

public interface UmsRoleDao {
    List<UmsMenu> getMenuList(Long adminId);
}
