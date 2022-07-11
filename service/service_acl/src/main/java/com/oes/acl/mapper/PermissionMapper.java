package com.oes.acl.mapper;

import com.oes.acl.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

// acl_permission表
public interface PermissionMapper extends BaseMapper<Permission> {

    List<String> selectPermissionValueByUserId(String id);

    // 获取所有权限
    List<String> selectAllPermissionValue();

    // 根据用户id选择权限
    List<Permission> selectPermissionByUserId(String userId);
}
