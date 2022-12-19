package com.chunlei.mall.service.Impl;

import com.chunlei.mall.dao.PmsProductAttributeCategoryDao;
import com.chunlei.mall.dao.PmsProductCategoryDao;
import com.chunlei.mall.dto.PmsProductAttributeCategoryItem;
import com.chunlei.mall.mapper.PmsProductAttributeCategoryMapper;
import com.chunlei.mall.mapper.PmsProductAttributeMapper;
import com.chunlei.mall.model.PmsProductAttributeCategory;
import com.chunlei.mall.model.PmsProductAttributeCategoryExample;
import com.chunlei.mall.service.PmsProductAttributeCategoryService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {

    @Autowired
    PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;
    @Autowired
    PmsProductAttributeCategoryDao pmsProductAttributeCategoryDao;

    @Override
    public List<PmsProductAttributeCategory> getProductAttributeCategoryList(int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductAttributeCategoryExample example = new PmsProductAttributeCategoryExample();
        return pmsProductAttributeCategoryMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getProductAttributeCategoryListWithAttr() {
        return pmsProductAttributeCategoryDao.getItemListWithAttr();
    }


}
