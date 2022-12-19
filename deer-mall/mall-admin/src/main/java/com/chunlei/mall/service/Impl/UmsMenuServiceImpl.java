package com.chunlei.mall.service.Impl;

import com.chunlei.mall.mapper.UmsMenuMapper;
import com.chunlei.mall.model.UmsMenu;
import com.chunlei.mall.model.UmsMenuExample;
import com.chunlei.mall.service.UmsMenuService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UmsMenuServiceImpl implements UmsMenuService {
    @Autowired
    UmsMenuMapper menuMapper;

    @Override
    public List<UmsMenu> getMenuList(Long parent_id, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        UmsMenuExample menuExample = new UmsMenuExample();
        if(parent_id != null){
            menuExample.createCriteria().andParentIdEqualTo(parent_id);
        }
        return menuMapper.selectByExample(menuExample);
    }
}
