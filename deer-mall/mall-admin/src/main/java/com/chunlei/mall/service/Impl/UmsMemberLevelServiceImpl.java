package com.chunlei.mall.service.Impl;

import com.chunlei.mall.mapper.UmsMemberLevelMapper;
import com.chunlei.mall.model.UmsMemberLevel;
import com.chunlei.mall.model.UmsMemberLevelExample;
import com.chunlei.mall.service.UmsMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {
    @Autowired
    UmsMemberLevelMapper umsMemberLevelMapper;

    @Override
    public List<UmsMemberLevel> getList(int defaultStatus) {
        UmsMemberLevelExample example = new UmsMemberLevelExample();
        example.createCriteria().andDefaultStatusEqualTo(defaultStatus);
        return umsMemberLevelMapper.selectByExample(example);
    }
}
