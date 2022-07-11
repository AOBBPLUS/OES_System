package com.oes.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.commonutils.JwtUtils;
import com.oes.commonutils.MD5Utils;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.servicebase.exceptionhandler.OesException;
import com.oes.ucenter.entity.UcenterMember;
import com.oes.ucenter.entity.vo.LoginVO;
import com.oes.ucenter.entity.vo.PasswordVO;
import com.oes.ucenter.entity.vo.RegisterVO;
import com.oes.ucenter.mapper.UcenterMemberMapper;
import com.oes.ucenter.service.UcenterMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-06-29
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 用户登录
    @Override
    public String userLogin(LoginVO loginVO) {
        if (loginVO == null) throw new NullPointerException();

        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        // 先对手机和密码进行非空验证
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new OesException(ResultCodeEnum.MOBILE_PASSWORD_CANT_NULL);
        }

        // 再判断手机号格式是否正确,减少数据库交互
        if (!Pattern.matches("^1[3-9]\\d{9}$", mobile)) {
            throw new OesException(ResultCodeEnum.MOBILE_INVALID_FORMAT);
        }

        // 接着判断手机是否正确,有没有在数据库中
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", loginVO.getMobile());
        UcenterMember ucenterMember = this.baseMapper.selectOne(wrapper);

        if (ucenterMember == null) {
            throw new OesException(ResultCodeEnum.MOBILE_NOT_REGISTERED);
        }

        // 运行到这里表示手机号密码不为空，且手机号正确，验证密码是否正确，此时的密码已经是被加密过后的
        if (!password.equals(ucenterMember.getPassword())) {
            throw new OesException(ResultCodeEnum.PASSWORD_ERROR);
        }

        // 到这里,手机号,密码校验完成,判断该用户是否被禁用
        if (ucenterMember.getIsDisabled()) {
            throw new OesException(ResultCodeEnum.USER_IS_DISABLED);
        }

        //程序运行到这里表示登录成功，生成token
        return JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
    }

    // 用户注册
    @Override
    public void userRegister(RegisterVO registerVO) {
        // 避免空语句
        if (registerVO == null) throw new NullPointerException();

        // 获取参数
        String mobile = registerVO.getMobile();
        String nickname = registerVO.getNickname();
        String code = registerVO.getCode();
        String password = registerVO.getPassword();

        // 非空验证
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname)
                || StringUtils.isEmpty(code) || StringUtils.isEmpty(password)) {
            throw new OesException(ResultCodeEnum.REGISTER_FAILED);
        }

        // 判断手机号是否重复
        UcenterMember mobileData = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("mobile", mobile));
        if (mobileData != null) throw new OesException(ResultCodeEnum.MOBILE_HAVE_REGISTERED);

        // 到这里表示输入不为空,验证验证码是否正确,从redis中取
        String MobileCode = redisTemplate.opsForValue().get(mobile);

        if (!code.equals(MobileCode)) {
            throw new OesException(ResultCodeEnum.AUTHENTICATION_CODE_ERROR);
        }

        // 到这里表示一切正确，将用户数据保存到数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setPassword(MD5Utils.getMD5(password));
        member.setNickname(nickname);
        member.setIsDisabled(false);
        member.setAvatar("https://img.51miz.com/Element/00/88/08/84/72f298b9_E880884_d0f63115.png");
        boolean save = this.save(member);

        if (!save) throw new OesException(ResultCodeEnum.REGISTER_FAILED);
    }

    // 根据微信id查询是否存在用户
    @Override
    public UcenterMember getMemberByOpenId(String openId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openId);
        return baseMapper.selectOne(wrapper);
    }

    // 修改用户密码
    @Override
    public void updatePassword(PasswordVO changeVO) {
        //获取注册的数据 校验参数
        String code = changeVO.getCode(); //验证码
        String mobile = changeVO.getMobile(); //手机号
        String password = changeVO.getPassword(); //密码

        //判断验证码
        //获取redis验证码
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!code.equals(redisCode)) {
            throw new OesException(ResultCodeEnum.AUTHENTICATION_CODE_ERROR);
        }

        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(changeVO, ucenterMember);
        int update = baseMapper.updateById(ucenterMember);
        if (update == 0) {
            throw new OesException(ResultCodeEnum.MODIFY_PASSWORD_FAILED);
        }

        //数据添加数据库中
        UcenterMember member = new UcenterMember();
        member.setMobile(mobile);
        member.setPassword(password);
        baseMapper.updateById(member);
    }

    // 注册人数统计
    @Override
    public int registerCount(String day) {
        return baseMapper.registerCount(day);
    }
}
