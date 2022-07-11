package com.oes.acl.controller;

import com.alibaba.fastjson.JSONObject;
import com.oes.acl.service.IndexService;
import com.oes.commonutils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "用于显示的服务")
@RestController
@RequestMapping("/admin/acl/index")
public class IndexController {
    private IndexService indexService;

    @Autowired
    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("info")
    public Result info() {
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo = indexService.getUserInfo(username);
        return Result.ok().data(userInfo);
    }

    @ApiOperation(value = "获取菜单")
    @GetMapping("menu")
    public Result getMenu() {
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JSONObject> permissionList = indexService.getMenu(username);
        return Result.ok().data("permissionList", permissionList);
    }

    @ApiOperation(value = "用户登出")
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
