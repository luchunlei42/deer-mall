package com.chunlei.mall.service;

import com.chunlei.mall.dto.PmsProductAttributeCategoryItem;
import com.chunlei.mall.model.PmsProductAttributeCategory;

import java.util.List;

public interface PmsProductAttributeCategoryService {

    public List<PmsProductAttributeCategory> getProductAttributeCategoryList(int pageSize, int pageNum);

    public List<PmsProductAttributeCategoryItem> getProductAttributeCategoryListWithAttr();

}
