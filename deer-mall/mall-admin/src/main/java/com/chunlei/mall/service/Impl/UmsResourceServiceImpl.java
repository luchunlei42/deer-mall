package com.chunlei.mall.service.Impl;

import com.chunlei.mall.mapper.UmsResourceMapper;
import com.chunlei.mall.model.UmsResource;
import com.chunlei.mall.model.UmsResourceExample;
import com.chunlei.mall.service.UmsResourceService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsResourceServiceImpl implements UmsResourceService {
    @Autowired
    UmsResourceMapper resourceMapper;

    @Override
    public List<UmsResource> getList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return resourceMapper.selectByExample(new UmsResourceExample());
    }
}
