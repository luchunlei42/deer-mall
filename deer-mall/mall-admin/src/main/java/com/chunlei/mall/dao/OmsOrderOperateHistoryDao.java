package com.chunlei.mall.dao;

import com.chunlei.mall.model.OmsOrderOperateHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmsOrderOperateHistoryDao {
    int insertList(@Param("list") List<OmsOrderOperateHistory> operateHistoryList);
}
