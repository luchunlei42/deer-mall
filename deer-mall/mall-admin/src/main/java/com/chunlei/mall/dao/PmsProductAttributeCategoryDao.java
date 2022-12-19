package com.chunlei.mall.dao;

import com.chunlei.mall.dto.PmsProductAttributeCategoryItem;

import java.util.List;

public interface PmsProductAttributeCategoryDao {
    public List<PmsProductAttributeCategoryItem> getItemListWithAttr();
}
