package com.chunlei.mall.service.Impl;

import com.chunlei.mall.mapper.UmsResourceCategoryMapper;
import com.chunlei.mall.model.UmsResourceCategory;
import com.chunlei.mall.model.UmsResourceCategoryExample;
import com.chunlei.mall.service.UmsResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsResourceCategoryServiceImpl implements UmsResourceCategoryService {
    @Autowired
    UmsResourceCategoryMapper resourceCategoryMapper;

    @Override
    public List<UmsResourceCategory> lisAll() {
        return resourceCategoryMapper.selectByExample(new UmsResourceCategoryExample());
    }
}
