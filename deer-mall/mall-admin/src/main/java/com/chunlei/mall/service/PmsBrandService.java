package com.chunlei.mall.service;

import com.chunlei.mall.model.PmsBrand;

import java.util.List;

public interface PmsBrandService {
    public List<PmsBrand> getBrandList(int pageNum, int pageSize);
}
