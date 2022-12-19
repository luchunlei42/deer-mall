package com.chunlei.mall.service;

import com.chunlei.mall.dto.PmsProductAttributeParam;
import com.chunlei.mall.model.PmsProductAttribute;

import java.util.List;

public interface PmsProductAttributeService {

    public int createAttr(PmsProductAttributeParam pmsProductAttributeParam);
    public List<PmsProductAttribute> getAttrList(Long cid, int pageNum, int pageSize);
}
