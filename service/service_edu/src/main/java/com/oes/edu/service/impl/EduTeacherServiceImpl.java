package com.oes.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.edu.entity.EduTeacher;
import com.oes.edu.entity.vo.TeacherQueryCondition;
import com.oes.edu.mapper.EduTeacherMapper;
import com.oes.edu.service.EduTeacherService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-06-24
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {
    @Override
    public void pageQueryByCondition(Page<EduTeacher> teacherPage, TeacherQueryCondition teacherQueryCondition) {
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (teacherQueryCondition == null) {
            baseMapper.selectPage(teacherPage, wrapper);
            return;
        }
        //多条件组合查询
        //类比mybatis学的动态sql
        String name = teacherQueryCondition.getName();
        Integer level = teacherQueryCondition.getLevel();
        String begin = teacherQueryCondition.getBegin();
        String end = teacherQueryCondition.getEnd();

        //判断条件是否为空，如果不为空拼接条件
        if (!StringUtils.isEmpty(name)) wrapper.like("name", name);
        if (!StringUtils.isEmpty(level)) wrapper.eq("level", level);
        if (!StringUtils.isEmpty(begin)) wrapper.ge("gmt_create", begin);
        if (!StringUtils.isEmpty(end)) wrapper.le("gmt_create", end);

        //排序
        wrapper.orderByDesc("gmt_create");

        //调用方法实现分页
        baseMapper.selectPage(teacherPage, wrapper);
    }

    // 按column进行排序,并限制只显示limit条
    @Cacheable(value = "teacher", key = "'limitQuery'")
    @Override
    public List<EduTeacher> limitQuery(String column, int limit) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(column);
        wrapper.last("limit " + limit);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> frontPageQuery(Page<EduTeacher> teacherPage) {
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        baseMapper.selectPage(teacherPage, wrapper);

        Map<String, Object> dataMap = new HashMap<>();

        // 将分页对象封装到map中
        dataMap.put("total", teacherPage.getTotal());
        dataMap.put("records", teacherPage.getRecords());
        dataMap.put("pages", teacherPage.getPages());
        dataMap.put("current", teacherPage.getCurrent());
        dataMap.put("size", teacherPage.getSize());
        dataMap.put("hasNext", teacherPage.hasNext());
        dataMap.put("hasPrevious", teacherPage.hasPrevious());

        // 返回map
        return dataMap;
    }
}
