package com.chunlei.mall.service;

import com.chunlei.mall.dto.PmsProductCategoryParam;
import com.chunlei.mall.dto.PmsProductCategoryWithChildrenItem;
import com.chunlei.mall.model.PmsProductCategory;

import java.util.List;

public interface PmsProductCategoryService {

    public List<PmsProductCategoryWithChildrenItem> getListWithChildren();

    public List<PmsProductCategory> getList(Long parent_id, int pageSize, int pageNum);

    public int create(PmsProductCategoryParam pmsProductCategoryParam);

}
