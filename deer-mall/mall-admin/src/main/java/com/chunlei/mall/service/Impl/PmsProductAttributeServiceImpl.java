package com.chunlei.mall.service.Impl;

import com.chunlei.mall.dto.PmsProductAttributeParam;
import com.chunlei.mall.mapper.PmsProductAttributeCategoryMapper;
import com.chunlei.mall.mapper.PmsProductAttributeMapper;
import com.chunlei.mall.model.PmsProductAttribute;
import com.chunlei.mall.model.PmsProductAttributeCategory;
import com.chunlei.mall.model.PmsProductAttributeExample;
import com.chunlei.mall.service.PmsProductAttributeService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {
    @Autowired
    PmsProductAttributeMapper pmsProductAttributeMapper;
    @Autowired
    PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;

    @Override
    public int createAttr(PmsProductAttributeParam pmsProductAttributeParam) {
        PmsProductAttribute pmsProductAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(pmsProductAttributeParam,pmsProductAttribute);
        int count = pmsProductAttributeMapper.insert(pmsProductAttribute);
        //更新属性分类数量
        PmsProductAttributeCategory pmsProductAttributeCategory = pmsProductAttributeCategoryMapper.selectByPrimaryKey(pmsProductAttribute.getProductAttributeCategoryId());
        if(pmsProductAttribute.getType() == 0){
            pmsProductAttributeCategory.setAttributeCount(pmsProductAttributeCategory.getAttributeCount() + 1);
        }else if(pmsProductAttribute.getType() == 1){
            pmsProductAttributeCategory.setParamCount(pmsProductAttributeCategory.getParamCount() + 1);
        }
        pmsProductAttributeCategoryMapper.updateByPrimaryKey(pmsProductAttributeCategory);
        return count;
    }

    @Override
    public List<PmsProductAttribute> getAttrList(Long cid, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.createCriteria().andProductAttributeCategoryIdEqualTo(cid);
        return pmsProductAttributeMapper.selectByExample(example);

    }
}
