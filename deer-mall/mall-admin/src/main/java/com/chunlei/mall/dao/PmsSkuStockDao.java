package com.chunlei.mall.dao;

import com.chunlei.mall.model.PmsSkuStock;

import java.util.List;

public interface PmsSkuStockDao {
    int insertList(List<PmsSkuStock> skuStockList);
}
