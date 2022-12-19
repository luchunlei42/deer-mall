package com.chunlei.mall.service.Impl;

import com.chunlei.mall.dao.PmsProductCategoryAttributeRelationDao;
import com.chunlei.mall.dao.PmsProductCategoryDao;
import com.chunlei.mall.dto.PmsProductCategoryParam;
import com.chunlei.mall.dto.PmsProductCategoryWithChildrenItem;
import com.chunlei.mall.mapper.PmsProductCategoryMapper;
import com.chunlei.mall.model.PmsProductCategory;
import com.chunlei.mall.model.PmsProductCategoryAttributeRelation;
import com.chunlei.mall.model.PmsProductCategoryExample;
import com.chunlei.mall.service.PmsProductCategoryService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class PmsProductCategoryServiceImpl implements PmsProductCategoryService {

    @Autowired
    PmsProductCategoryDao pmsProductCategoryDao;
    @Autowired
    PmsProductCategoryMapper pmsProductCategoryMapper;
    @Autowired
    PmsProductCategoryAttributeRelationDao pmsProductCategoryAttributeRelationDao;

    @Override
    public List<PmsProductCategoryWithChildrenItem> getListWithChildren() {
        return pmsProductCategoryDao.getListWithChildren();
    }

    @Override
    public List<PmsProductCategory> getList(Long parent_id, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andParentIdEqualTo(parent_id);
        List<PmsProductCategory> list = pmsProductCategoryMapper.selectByExample(example);
        return list;
    }
    @Override
    public int create(PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory pmsProductCategory = new PmsProductCategory();
        pmsProductCategory.setProductCount(0);
        BeanUtils.copyProperties(pmsProductCategoryParam,pmsProductCategory);
        setCategoryLevel(pmsProductCategory);
        int count = pmsProductCategoryMapper.insert(pmsProductCategory);
        List<Long> productAttributeIdList = pmsProductCategoryParam.getProductAttributeIdList();
        if(!CollectionUtils.isEmpty(productAttributeIdList)){
            insertRelationList(pmsProductCategory.getId(),productAttributeIdList);
        }
        return count;
    }
    private void setCategoryLevel(PmsProductCategory pmsProductCategory){
        if(pmsProductCategory.getParentId() == 0){
            pmsProductCategory.setLevel(0);
        } else{
            PmsProductCategory parentCategory = pmsProductCategoryMapper.selectByPrimaryKey(pmsProductCategory.getParentId());
            if(parentCategory != null){
                pmsProductCategory.setLevel(parentCategory.getLevel()+1);
            }else{
                pmsProductCategory.setLevel(0);
            }
        }
    }

    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList){
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for(Long productAttrId: productAttributeIdList){
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductCategoryId(productCategoryId);
            relation.setProductAttributeId(productAttrId);
            relationList.add(relation);
        }
        pmsProductCategoryAttributeRelationDao.insertList(relationList);
    }
}
