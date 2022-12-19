package com.chunlei.mall.service;

import com.chunlei.mall.model.UmsResource;

import java.util.List;

public interface UmsResourceService {
    List<UmsResource> getList(int pageNum, int pageSize);
}
