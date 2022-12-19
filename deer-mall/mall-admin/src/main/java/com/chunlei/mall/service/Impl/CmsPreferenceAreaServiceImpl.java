package com.chunlei.mall.service.Impl;

import com.chunlei.mall.mapper.CmsPrefrenceAreaMapper;
import com.chunlei.mall.model.CmsPrefrenceArea;
import com.chunlei.mall.model.CmsPrefrenceAreaExample;
import com.chunlei.mall.service.CmsPreferenceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CmsPreferenceAreaServiceImpl implements CmsPreferenceAreaService {
    @Autowired
    CmsPrefrenceAreaMapper prefrenceAreaMapper;

    @Override
    public List<CmsPrefrenceArea> listAll() {
        return prefrenceAreaMapper.selectByExample(new CmsPrefrenceAreaExample());
    }
}
