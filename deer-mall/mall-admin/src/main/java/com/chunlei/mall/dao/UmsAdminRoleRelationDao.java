package com.chunlei.mall.dao;

import com.chunlei.mall.model.UmsResource;
import com.chunlei.mall.model.UmsRole;

import java.util.List;

public interface UmsAdminRoleRelationDao {
    List<UmsRole> getRoleList(Long adminId);

    List<UmsResource> getResourceList(Long adminId);
}
