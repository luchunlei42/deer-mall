package com.chunlei.mall.service;

import com.chunlei.mall.model.OmsOrderSetting;

public interface OmsOrderSettingService {
    OmsOrderSetting getItem(Long id);
    int update(Long id, OmsOrderSetting orderSetting);
}
