package com.chunlei.mall.dao;

import com.chunlei.mall.model.PmsMemberPrice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsMemberPriceDao {
    public int insertList(@Param("memberPriceList") List<PmsMemberPrice> memberPriceList);
}
