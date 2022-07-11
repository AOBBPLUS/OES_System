package com.oes.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oes.edu.entity.EduTeacher;
import com.oes.edu.entity.vo.TeacherQueryCondition;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author zh
 * @since 2022-06-24
 */
public interface EduTeacherService extends IService<EduTeacher> {
    // 条件分页查询
    void pageQueryByCondition(Page<EduTeacher> teacherPage, TeacherQueryCondition teacherQueryCondition);

    // 按column进行排序,并限制只显示limit条
    List<EduTeacher> limitQuery(String column, int limit);

    // 分页查询,为学员端使用
    Map<String, Object> frontPageQuery(Page<EduTeacher> teacherPage);
}
