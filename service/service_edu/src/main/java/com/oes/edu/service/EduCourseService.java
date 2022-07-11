package com.oes.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oes.edu.entity.EduCourse;
import com.oes.edu.entity.vo.CourseListVO;
import com.oes.edu.entity.vo.frontvo.CourseFrontQueryCondition;
import com.oes.edu.entity.vo.CourseInfoVO;
import com.oes.edu.entity.vo.CoursePublishVO;
import com.oes.edu.entity.vo.CourseQueryCondition;
import com.oes.edu.entity.vo.frontvo.CourseWebVO;

import java.util.List;
import java.util.Map;

// 课程服务类
public interface EduCourseService extends IService<EduCourse> {
    // 添加课程基本信息
    String addCourseInfo(CourseInfoVO courseInfo);

    // 根据课程id获取课程信息
    CourseInfoVO getCourseInfoById(String courseId);

    // 根据课程id修改课程信息
    void updateCourseInfo(CourseInfoVO courseInfo);

    // 根据id,获得课程信息-讲师-章节
    CoursePublishVO getPublishCourseInfoById(String courseId);

    // 根据课程id发布课程
    void publishCourse(String courseId);

    // 分页条件查询
    void pageQuery(Page<EduCourse> coursePage, CourseQueryCondition courseQueryCondition);

    // 根据课程id删除课程
    void deleteCourseById(String courseId);

    // 按column进行排序,并限制只显示limit条
    List<EduCourse> limitQuery(String column, int limit);

    // 根据讲师id查询讲师课程
    List<EduCourse> getTeacherCourseList(String teacherId);

    // 学员端分页查询
    Map<String, Object> frontPageQuery(Page<EduCourse> coursePage, CourseFrontQueryCondition courseFrontQueryCondition);

    // 学员端根据课程id查询课程信息
    CourseWebVO getFrontCourseInfoById(String courseId);

    // 管理员端获取所有课程信息
    List<CourseListVO> getAllCourseInfo();
}
