package com.chunlei.mall.service;

import com.chunlei.mall.model.UmsMenu;

import java.util.List;

public interface UmsMenuService {
    List<UmsMenu> getMenuList(Long parent_id, int pageNum, int pageSize);
}
