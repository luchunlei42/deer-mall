package com.chunlei.mall.service;

import com.chunlei.mall.model.UmsMemberLevel;

import java.util.List;

public interface UmsMemberLevelService {
    public List<UmsMemberLevel> getList(int defaultStatus);
}
