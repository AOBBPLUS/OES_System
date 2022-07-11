package com.oes.acl.service;

import com.alibaba.fastjson.JSONObject;
import com.oes.acl.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    // 获取全部菜单
    List<Permission> queryAllMenu();

    // 根据角色获取菜单
    List<Permission> selectAllMenu(String roleId);

    // 给角色分配权限
    void saveRolePermissionRelationShip(String roleId, String[] permissionId);

    // 递归删除菜单
    void removeChildById(String id);

    // 根据用户id获取用户菜单
    List<String> selectPermissionValueByUserId(String id);

    // 选择权限
    List<JSONObject> selectPermissionByUserId(String id);
}
