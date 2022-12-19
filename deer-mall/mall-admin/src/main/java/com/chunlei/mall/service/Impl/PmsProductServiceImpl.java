package com.chunlei.mall.service.Impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.chunlei.mall.dao.*;
import com.chunlei.mall.dto.PmsProductCategoryParam;
import com.chunlei.mall.dto.PmsProductParam;
import com.chunlei.mall.dto.PmsProductQueryParam;
import com.chunlei.mall.mapper.*;
import com.chunlei.mall.model.*;
import com.chunlei.mall.service.PmsProductService;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PmsProductServiceImpl implements PmsProductService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);

    @Autowired
    PmsProductMapper pmsProductMapper;
    @Autowired
    PmsMemberPriceDao pmsMemberPriceDao;
    @Autowired
    PmsProductLadderDao pmsProductLadderDao;
    @Autowired
    PmsProductFullReductionDao pmsProductFullReductionDao;
    @Autowired
    PmsSkuStockDao pmsSkuStockDao;
    @Autowired
    PmsProductAttributeValueDao pmsProductAttributeValueDao;
    @Autowired
    CmsSubjectProductRelationDao cmsSubjectProductRelationDao;
    @Autowired
    CmsPrefrenceAreaProductRelationDao cmsPrefrenceAreaProductRelationDao;
    @Autowired
    PmsMemberPriceMapper pmsMemberPriceMapper;
    @Autowired
    PmsProductLadderMapper productLadderMapper;
    @Autowired
    PmsProductFullReductionMapper pmsProductFullReductionMapper;
    @Autowired
    PmsSkuStockMapper skuStockMapper;
    @Autowired
    PmsProductAttributeValueMapper productAttributeValueMapper;
    @Autowired
    CmsSubjectProductRelationMapper subjectProductRelationMapper;
    @Autowired
    CmsPrefrenceAreaProductRelationMapper cmsPrefrenceAreaProductRelationMapper;

    @Override
    public List<PmsProduct> getProductList(PmsProductQueryParam pmsProductQueryParam, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        PmsProductExample pmsProductExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = pmsProductExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if(pmsProductQueryParam.getProductCategoryId() != null){
            criteria.andProductCategoryIdEqualTo(pmsProductQueryParam.getProductCategoryId());
        }
        if(pmsProductQueryParam.getProductSn() != null){
            criteria.andProductSnEqualTo(pmsProductQueryParam.getProductSn());
        }
        if(pmsProductQueryParam.getBrandId() != null){
            criteria.andBrandIdEqualTo(pmsProductQueryParam.getBrandId());
        }
        if(pmsProductQueryParam.getVerifyStatus() != null){
            criteria.andVerifyStatusEqualTo(pmsProductQueryParam.getVerifyStatus());
        }
        if(pmsProductQueryParam.getPublishStatus() != null){
            criteria.andPublishStatusEqualTo(pmsProductQueryParam.getPublishStatus());
        }
        if(pmsProductQueryParam.getKeyword() != null){
            criteria.andNameLike("%"+pmsProductQueryParam.getKeyword()+"%");
        }
        //pmsProductExample.setOrderByClause("sort desc");
        return pmsProductMapper.selectByExample(pmsProductExample);
    }

    @Override
    public int create(PmsProductParam pmsProductParam) {
        int count;
        PmsProduct product = new PmsProduct();
        BeanUtils.copyProperties(pmsProductParam,product);
        product.setId(null);
        pmsProductMapper.insertSelective(product);
        Long productId = product.getId();
        //根据促销类型设置价格：会员价格，阶梯价格，满减价格
        relateAndInsertList(pmsMemberPriceDao,pmsProductParam.getMemberPriceList(),productId);
        //阶梯价格
        relateAndInsertList(pmsProductLadderDao,pmsProductParam.getProductLadderList(),productId);
        //满减价格
        relateAndInsertList(pmsProductFullReductionDao,pmsProductParam.getProductFullReductionList(),productId);
        //处理sku的编码
        handleSkuStockCode(pmsProductParam.getSkuStockList(),productId);
        //添加sku库存信息
        relateAndInsertList(pmsSkuStockDao,pmsProductParam.getSkuStockList(),productId);
        //添加商品参数，添加自定义商品规格
        relateAndInsertList(pmsProductAttributeValueDao, pmsProductParam.getProductAttributeValueList(),productId);
        //关联专题
        relateAndInsertList(cmsSubjectProductRelationDao,pmsProductParam.getSubjectProductRelationList(),productId);
        //关联优选
        relateAndInsertList(cmsPrefrenceAreaProductRelationDao,pmsProductParam.getPrefrenceAreaProductRelationList(),productId);
        count = 1;
        return count;
    }

    @Override
    public int update(Long id, PmsProductParam pmsProductParam) {
        int count;
        PmsProduct product = pmsProductParam;
        product.setId(id);
        pmsProductMapper.updateByPrimaryKey(product);
        //会员价格
        PmsMemberPriceExample pmsMemberPriceExample = new PmsMemberPriceExample();
        pmsMemberPriceExample.createCriteria().andIdEqualTo(id);
        pmsMemberPriceMapper.deleteByExample(pmsMemberPriceExample);
        relateAndInsertList(pmsMemberPriceDao,pmsProductParam.getMemberPriceList(),id);
        //阶梯价格
        PmsProductLadderExample ladderExample = new PmsProductLadderExample();
        ladderExample.createCriteria().andProductIdEqualTo(id);
        productLadderMapper.deleteByExample(ladderExample);
        relateAndInsertList(pmsProductLadderDao, pmsProductParam.getProductLadderList(), id);
        //满减价格
        PmsProductFullReductionExample fullReductionExample = new PmsProductFullReductionExample();
        fullReductionExample.createCriteria().andProductIdEqualTo(id);
        pmsProductFullReductionMapper.deleteByExample(fullReductionExample);
        relateAndInsertList(pmsProductFullReductionDao, pmsProductParam.getProductFullReductionList(), id);
        //修改sku库存信息
        handleUpdateSkuStockCode(pmsProductParam.getSkuStockList(),id);
        //修改商品参数,添加自定义商品规格
        PmsProductAttributeValueExample pmsProductAttributeValueExample = new PmsProductAttributeValueExample();
        pmsProductAttributeValueExample.createCriteria().andIdEqualTo(id);
        productAttributeValueMapper.deleteByExample(pmsProductAttributeValueExample);
        relateAndInsertList(pmsProductAttributeValueDao,pmsProductParam.getProductAttributeValueList(),id);
        //关联主题
        CmsSubjectProductRelationExample subjectProductRelationExample = new CmsSubjectProductRelationExample();
        subjectProductRelationExample.createCriteria().andProductIdEqualTo(id);
        subjectProductRelationMapper.deleteByExample(subjectProductRelationExample);
        relateAndInsertList(cmsSubjectProductRelationDao, pmsProductParam.getSubjectProductRelationList(), id);
        //关联优选
        CmsPrefrenceAreaProductRelationExample prefrenceAreaExample = new CmsPrefrenceAreaProductRelationExample();
        prefrenceAreaExample.createCriteria().andProductIdEqualTo(id);
        cmsPrefrenceAreaProductRelationMapper.deleteByExample(prefrenceAreaExample);
        relateAndInsertList(cmsPrefrenceAreaProductRelationDao, pmsProductParam.getPrefrenceAreaProductRelationList(), id);
        count = 1;
        return count;
    }

    private void relateAndInsertList(Object dao, List dataList, Long productId){
        try {
            if(CollectionUtils.isEmpty(dataList)) return;
            for (Object item : dataList){
                Method setId = item.getClass().getMethod("setId",Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }
            Method insertList = dao.getClass().getMethod("insertList", List.class);
            insertList.invoke(dao,dataList);
        }catch (Exception e){
            LOGGER.warn("创建产品出错：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId){
        if(CollectionUtils.isEmpty(skuStockList))return;
        for (int i=0;i<skuStockList.size();i++){
            PmsSkuStock skuStock = skuStockList.get(i);
            if(StrUtil.isEmpty(skuStock.getSkuCode())){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                //日期
                sb.append(sdf.format(new Date()));
                //四位商品id
                sb.append(String.format("%04d", productId));
                //三位索引id
                sb.append(String.format("%03d",i+1));
                skuStock.setSkuCode(sb.toString());
            }
        }
    }

    private void handleUpdateSkuStockCode(List<PmsSkuStock> skuStockList, Long id){
        if(CollUtil.isEmpty(skuStockList)){
            //没有sku信息直接删除
            PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
            skuStockExample.createCriteria().andIdEqualTo(id);
            skuStockMapper.deleteByExample(skuStockExample);
        }
        //获取初始sku信息
        PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
        skuStockExample.createCriteria().andIdEqualTo(id);
        List<PmsSkuStock> oriSkuStockList = skuStockMapper.selectByExample(skuStockExample);
        //获取新增sku信息
        List<PmsSkuStock> insertSkuList = skuStockList.stream().filter(item->item.getId()==null).collect(Collectors.toList());
        //获取需要更新的sku信息
        List<PmsSkuStock> updateSkuList = skuStockList.stream().filter(item->item.getId()!=null).collect(Collectors.toList());
        List<Long> updateSkuIds = updateSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
        //获取需要删除的sku信息
        List<PmsSkuStock> removeSkuList = oriSkuStockList.stream().filter(item->!updateSkuIds.contains(item.getId())).collect(Collectors.toList());;
        handleSkuStockCode(insertSkuList,id);
        handleSkuStockCode(updateSkuList,id);
        //新增sku
        if(CollUtil.isNotEmpty(insertSkuList)){
            relateAndInsertList(pmsSkuStockDao,insertSkuList,id);
        }
        if(CollUtil.isNotEmpty(removeSkuList)){
            List<Long> removeSkuIds = removeSkuList.stream().map(PmsSkuStock::getId).collect(Collectors.toList());
            PmsSkuStockExample removeExample = new PmsSkuStockExample();
            removeExample.createCriteria().andIdIn(removeSkuIds);
            skuStockMapper.deleteByExample(removeExample);
        }
        //修改sku
        if(CollUtil.isNotEmpty(updateSkuList)){
            for(PmsSkuStock pmsSkuStock: updateSkuList){
                skuStockMapper.updateByPrimaryKeySelective(pmsSkuStock);
            }
        }
    }

}
