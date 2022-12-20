package com.chunlei.mall.service.Impl;

import com.chunlei.mall.dto.OmsOrderDetail;
import com.chunlei.mall.dto.OmsOrderQueryParam;
import com.chunlei.mall.mapper.OmsOrderSettingMapper;
import com.chunlei.mall.model.OmsOrder;
import com.chunlei.mall.model.OmsOrderSetting;
import com.chunlei.mall.service.OmsOrderService;
import com.chunlei.mall.service.OmsOrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {
    @Autowired
    private OmsOrderSettingMapper orderSettingMapper;

    @Override
    public OmsOrderSetting getItem(Long id) {
        return orderSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, OmsOrderSetting orderSetting) {
        orderSetting.setId(id);
        return orderSettingMapper.updateByPrimaryKey(orderSetting);
    }
}
