package com.chunlei.mall;

import com.chunlei.mall.util.DynamicDbUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDbUtil.get();
    }
}
