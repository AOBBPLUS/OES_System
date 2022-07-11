package com.oes.acl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.acl.entity.Permission;
import com.oes.acl.entity.RolePermission;
import com.oes.acl.entity.User;
import com.oes.acl.helper.MenuHelper;
import com.oes.acl.helper.PermissionHelper;
import com.oes.acl.mapper.PermissionMapper;
import com.oes.acl.service.PermissionService;
import com.oes.acl.service.RolePermissionService;
import com.oes.acl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    private RolePermissionService rolePermissionService;

    private UserService userService;

    @Autowired
    public void setRolePermissionService(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //获取全部菜单
    @Override
    public List<Permission> queryAllMenu() {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        // 先按照升序将所有权限获取到
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        return PermissionHelper.build(permissionList);
    }

    //根据角色获取菜单
    @Override
    public List<Permission> selectAllMenu(String roleId) {
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));
        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        //转换给角色id与角色权限对应Map对象
        for (Permission permission : allPermissionList) {
            for (RolePermission rolePermission : rolePermissionList) {
                if (rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }
        return PermissionHelper.build(allPermissionList);
    }

    //给角色分配权限
    @Override
    public void saveRolePermissionRelationShip(String roleId, String[] permissionIds) {

        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));

        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (String permissionId : permissionIds) {
            if (StringUtils.isEmpty(permissionId)) continue;

            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        // 加上acl_permission中id为1,pid为0的那条“全部数据的权限”,这个错误是前端的问题,前端没把根节点也就是"全部数据"传过来
        rolePermissionService.saveBatch(rolePermissionList);
    }

    // 递归删除菜单
    @Override
    public void removeChildById(String id) {
        List<String> idList = new ArrayList<>();
        this.selectChildListById(id, idList);

        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    // 根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList;
        if (this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限，对应表中的permission_value
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        //selectPermissionValueList应该要去重,以应对多重身份的情况
        Set<String> permissionSet = new HashSet<>(selectPermissionValueList);
        return new ArrayList<>(permissionSet);
    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList;
        if (this.isSysAdmin(userId)) {
            // 如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            // 否则则应该根据用户id查询权限
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }
        Set<Permission> permissionSet = new HashSet<>(selectPermissionList);
        List<Permission> permissionList = new ArrayList<>(permissionSet);
        // permissionList应该按id排序
        permissionList.sort(Comparator.comparing(Permission::getId));
        return MenuHelper.build(PermissionHelper.build(permissionList));
    }

    // 判断用户是否系统管理员
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);
        return null != user && "admin".equals(user.getUsername());
    }

    // 递归获取子节点
    private void selectChildListById(String id, List<String> idList) {
        List<Permission> childList = baseMapper.selectList(new QueryWrapper<Permission>().eq("pid", id).select("id"));
        childList.stream().forEach(item -> {
            idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });
    }
}
