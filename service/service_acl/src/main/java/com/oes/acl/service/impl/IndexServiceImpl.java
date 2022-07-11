package com.oes.acl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.oes.acl.entity.Role;
import com.oes.acl.entity.User;
import com.oes.acl.service.IndexService;
import com.oes.acl.service.PermissionService;
import com.oes.acl.service.RoleService;
import com.oes.acl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {
    private UserService userService;

    private RoleService roleService;

    private PermissionService permissionService;

    private RedisTemplate<String,List<String>> redisTemplate;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, List<String>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据用户名获取用户登录信息
     *
     * @param username
     * @return
     */
    public Map<String, Object> getUserInfo(String username) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.selectByUsername(username);

        // 根据用户id获取角色
        List<Role> roleList = roleService.selectRoleByUserId(user.getId());
        // 把获得的所有角色名称放在List集合中
        List<String> roleNameList = roleList.stream().map(Role::getRoleName).collect(Collectors.toList());
        // admin就没有角色，但是admin可以获取所有权限
        if (roleNameList.size() == 0) {
            //前端框架必须返回一个角色，否则报错，如果没有角色，返回一个空角色
            roleNameList.add("admin");
        }

        //根据用户id获取操作权限值
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        // 更新用户在redis中存储的信息,没必要,毕竟在TokenLoginFilter.java中刚添加,没必要更新
        redisTemplate.opsForValue().set(username, permissionValueList);

        result.put("name", user.getUsername());
        result.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        result.put("roles", roleNameList);
        result.put("permissionValueList", permissionValueList);
        return result;
    }

    // 根据用户名获取动态菜单,用户名是唯一的
    public List<JSONObject> getMenu(String username) {
        User user = userService.selectByUsername(username);
        //根据用户id获取用户菜单权限
        return permissionService.selectPermissionByUserId(user.getId());
    }
}
