package com.oes.acl.helper;

import com.alibaba.fastjson.JSONObject;
import com.oes.acl.entity.Permission;
import io.swagger.annotations.ApiModel;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "根据权限数据构建登录用户左侧菜单数据", description = "根据权限数据构建登录用户左侧菜单数据")
public class MenuHelper {

    // 构建菜单
    public static List<JSONObject> build(List<Permission> treeNodes) {
        List<JSONObject> menus = new ArrayList<>();
        if (treeNodes.size() == 1) {
            Permission topNode = treeNodes.get(0);
            //左侧一级菜单
            List<Permission> oneMenuList = topNode.getChildren();
            for (Permission one : oneMenuList) {
                JSONObject oneMenu = new JSONObject();
                oneMenu.put("path", one.getPath());
                oneMenu.put("component", one.getComponent());
                oneMenu.put("redirect", one.getRedirect());
                oneMenu.put("name", "name_" + one.getId());
                oneMenu.put("hidden", one.getHidden());

                JSONObject oneMeta = new JSONObject();
                oneMeta.put("title", one.getName());
                oneMeta.put("icon", one.getIcon());
                oneMenu.put("meta", oneMeta);

                List<JSONObject> children = new ArrayList<>();
                List<Permission> twoMenuList = one.getChildren();
                for (Permission two : twoMenuList) {
                    // 二级和三级的菜单都是一级菜单下面
                    addPermissionToList(children, two);

                    List<Permission> threeMenuList = two.getChildren();
                    for (Permission three : threeMenuList) {
                        if (StringUtils.isEmpty(three.getPath())) continue;
                        // 二级和三级的菜单都是一级菜单下面
                        addPermissionToList(children, three);
                    }
                }
                oneMenu.put("children", children);
                menus.add(oneMenu);
            }
        }
        return menus;
    }

    // 将permission加入到children中
    private static void addPermissionToList(List<JSONObject> children, Permission permission) {
        JSONObject threeMenu = new JSONObject();
        threeMenu.put("path", permission.getPath());
        threeMenu.put("component", permission.getComponent());
        threeMenu.put("name", "name_" + permission.getId());
        threeMenu.put("hidden", permission.getHidden());

        JSONObject threeMeta = new JSONObject();
        threeMeta.put("title", permission.getName());
        threeMenu.put("meta", threeMeta);

        children.add(threeMenu);
    }
}