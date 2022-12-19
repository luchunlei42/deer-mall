package com.chunlei.mall.dao;

import com.chunlei.mall.dto.PmsProductCategoryWithChildrenItem;

import java.util.List;

public interface PmsProductCategoryDao {

    public List<PmsProductCategoryWithChildrenItem> getListWithChildren();

}
