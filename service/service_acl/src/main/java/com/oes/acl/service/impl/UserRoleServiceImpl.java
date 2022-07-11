package com.oes.acl.service.impl;

import com.oes.acl.entity.UserRole;
import com.oes.acl.mapper.UserRoleMapper;
import com.oes.acl.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
