package com.oes.ucenter.mapper;

import com.oes.ucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2022-06-29
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    Integer registerCount(String day);
}
