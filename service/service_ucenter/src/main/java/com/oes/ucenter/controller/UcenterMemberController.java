package com.oes.ucenter.controller;

import com.oes.commonutils.JwtUtils;
import com.oes.commonutils.Result;
import com.oes.commonutils.ObjectFiledIsNull;
import com.oes.servicebase.vo.UcenterMemberVO;
import com.oes.ucenter.entity.UcenterMember;
import com.oes.ucenter.entity.vo.LoginVO;
import com.oes.ucenter.entity.vo.PasswordVO;
import com.oes.ucenter.entity.vo.RegisterVO;
import com.oes.ucenter.service.UcenterMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "学员用户登录管理控制器")
@RestController
@RequestMapping("/ucenterservice/member")
public class UcenterMemberController {
    private UcenterMemberService memberService;

    // 推荐setter注入
    @Autowired
    public void setMemberService(UcenterMemberService memberService) {
        this.memberService = memberService;
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("userLogin")
    public Result userLogin(
            @ApiParam(name = "loginVO", value = "登录需要的数据", required = true)
            @RequestBody LoginVO loginVO) {
        String token = memberService.userLogin(loginVO);
        return Result.ok().data("token", token);
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("userRegister")
    public Result userRegister(
            @ApiParam(name = "registerVO", value = "请求体", required = true)
            @RequestBody RegisterVO registerVO) {
        Result tempResult = ObjectFiledIsNull.isFiledHaveNull(registerVO,"注册信息不能为空");
        if (!tempResult.getSuccess()) {
            return tempResult;
        }
        memberService.userRegister(registerVO);
        return Result.ok();
    }

    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("getUserInfo")
    public Result getUserInfo(
            @ApiParam(name = "request", value = "请求体", required = true)
                    HttpServletRequest request) {
        // 调用jwt工具类，根据request对象获取头信息，返回用户id
        String memberIdByJwtToken = JwtUtils.getMemberIdByJwtToken(request);
        // 根据用户id查询用户信息
        UcenterMember member = memberService.getById(memberIdByJwtToken);
        // 不显示密码,防止破解
        member.setPassword("");
        return Result.ok().data("userInfo", member);
    }

    @ApiOperation(value = "修改用户信息")
    @PostMapping("updateUserInfo")
    public Result updateUserInfo(
            @ApiParam(name = "member", value = "用户", required = true)
            @RequestBody(required = true) UcenterMember member) {
        // 先获取密码再修改
        UcenterMember password = memberService.getById(member);
        member.setPassword(password.getPassword());
        return memberService.updateById(member) ? Result.ok() : Result.error();
    }

    //更改密码
    @ApiOperation(value = "更改密码")
    @PostMapping("updatePassword")
    public Result updatePassword(
            @ApiParam(name = "passwordVO", value = "修改密码的实体", required = true)
            @RequestBody(required = true) PasswordVO changeVO) {
        // 属性不能为空
        Result tempResult = ObjectFiledIsNull.isFiledHaveNull(changeVO, "修改密码数据不能为空");
        if (!tempResult.getSuccess()) return tempResult;

        memberService.updatePassword(changeVO);
        return Result.ok().message("修改密码成功");
    }

    @ApiOperation(value = "远程调用方法统计当日注册人数")
    @GetMapping("remoteRegisterCount/{day}")
    public Result remoteRegisterCount(
            @ApiParam(name = "day", value = "要统计的那一天", required = true)
            @PathVariable String day) {
        int registerCount = memberService.registerCount(day);
        return Result.ok().data("count", registerCount);
    }

    @ApiOperation(value = "远程调用方法根据id获取用户信息")
    @GetMapping("remoteGetUserInfo/{id}")
    public UcenterMemberVO remoteGetUserInfo(
            @ApiParam(name = "id", value = "用户id", required = true)
            @PathVariable String id) {
        UcenterMember member = memberService.getById(id);
        UcenterMemberVO ucenterMemberVo = new UcenterMemberVO();
        BeanUtils.copyProperties(member, ucenterMemberVo);
        return ucenterMemberVo;
    }
}

