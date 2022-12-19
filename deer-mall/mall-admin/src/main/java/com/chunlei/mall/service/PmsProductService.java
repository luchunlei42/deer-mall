package com.chunlei.mall.service;

import com.chunlei.mall.dto.PmsProductCategoryParam;
import com.chunlei.mall.dto.PmsProductParam;
import com.chunlei.mall.dto.PmsProductQueryParam;
import com.chunlei.mall.model.PmsProduct;

import java.util.List;

public interface PmsProductService {

    public List<PmsProduct> getProductList(PmsProductQueryParam pmsProductQueryParam, int pageNum, int pageSize);

    public int create(PmsProductParam pmsProductParam);

    public int update(Long id, PmsProductParam pmsProductParam);

}
