package com.chunlei.mall.dto;

import com.chunlei.mall.model.PmsProductAttribute;
import com.chunlei.mall.model.PmsProductAttributeCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory {

    @Setter
    @Getter
    @ApiModelProperty("该属性类型下的属性列表")
    List<PmsProductAttribute> productAttributeList;
}
