package com.oes.ucenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oes.ucenter.entity.UcenterMember;
import com.oes.ucenter.entity.vo.LoginVO;
import com.oes.ucenter.entity.vo.PasswordVO;
import com.oes.ucenter.entity.vo.RegisterVO;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author zh
 * @since 2022-06-29
 */
public interface UcenterMemberService extends IService<UcenterMember> {
    // 用户登录
    String userLogin(LoginVO loginVo);

    // 用户注册
    void userRegister(RegisterVO registerVo);

    // 根据openId查询是否存在用户
    UcenterMember getMemberByOpenId(String openId);

    // 修改用户密码
    void updatePassword(PasswordVO changeVO);

    // 统计用户注册
    int registerCount(String day);
}
