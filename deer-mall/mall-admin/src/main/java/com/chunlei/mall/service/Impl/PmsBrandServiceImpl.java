package com.chunlei.mall.service.Impl;

import com.chunlei.mall.mapper.PmsBrandMapper;
import com.chunlei.mall.model.PmsBrand;
import com.chunlei.mall.model.PmsBrandExample;
import com.chunlei.mall.service.PmsBrandService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsBrandServiceImpl implements PmsBrandService {

    @Autowired
    private PmsBrandMapper pmsBrandMapper;

    @Override
    public List<PmsBrand> getBrandList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsBrandExample pmsBrandExample = new PmsBrandExample();
        pmsBrandExample.setOrderByClause("sort desc");
        return pmsBrandMapper.selectByExample(pmsBrandExample);
    }
}
