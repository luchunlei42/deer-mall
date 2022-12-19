package com.chunlei.mall.service.Impl;

import com.chunlei.mall.dao.UmsRoleDao;
import com.chunlei.mall.mapper.UmsRoleMapper;
import com.chunlei.mall.model.UmsMenu;
import com.chunlei.mall.model.UmsRole;
import com.chunlei.mall.model.UmsRoleExample;
import com.chunlei.mall.service.UmsRoleService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;

@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    @Autowired
    private UmsRoleDao umsRoleDao;
    @Autowired
    UmsRoleMapper umsRoleMapper;

    @Override
    public List<UmsMenu> getMenuList(Long uerId) {
        return umsRoleDao.getMenuList(uerId);
    }

    @Override
    public List<UmsRole> listAll() {
        return umsRoleMapper.selectByExample(new UmsRoleExample());
    }

    @Override
    public List<UmsRole> getRoleList(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        UmsRoleExample roleExample = new UmsRoleExample();
        if(keyword != null){
            roleExample.createCriteria().andNameLike("%"+keyword+"%");
        }
        return umsRoleMapper.selectByExample(roleExample);
    }
}
