package com.oes.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.edu.entity.EduCourse;
import com.oes.edu.entity.EduCourseDescription;
import com.oes.edu.entity.vo.CourseInfoVO;
import com.oes.edu.entity.vo.CourseListVO;
import com.oes.edu.entity.vo.CoursePublishVO;
import com.oes.edu.entity.vo.CourseQueryCondition;
import com.oes.edu.entity.vo.frontvo.CourseFrontQueryCondition;
import com.oes.edu.entity.vo.frontvo.CourseWebVO;
import com.oes.edu.mapper.EduCourseMapper;
import com.oes.edu.service.*;
import com.oes.servicebase.exceptionhandler.OesException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-06-27
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    // 课程描述服务类
    private EduCourseDescriptionService descriptionService;

    // 讲师服务
    private EduTeacherService teacherService;

    // 小节服务类
    private EduVideoService videoService;

    // 章节服务类
    private EduChapterService chapterService;

    @Autowired
    public void setDescriptionService(EduCourseDescriptionService descriptionService) {
        this.descriptionService = descriptionService;
    }

    @Autowired
    public void setTeacherService(EduTeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setVideoService(EduVideoService videoService) {
        this.videoService = videoService;
    }

    @Autowired
    public void setChapterService(EduChapterService chapterService) {
        this.chapterService = chapterService;
    }

    // 添加课程信息
    @Override
    public String addCourseInfo(CourseInfoVO courseInfo) {
        // 1.向课程表中添加数据
        // 将CourseInfo转化为EduCourse
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfo, eduCourse);
        int insert = this.baseMapper.insert(eduCourse);
        // 添加失败则抛异常
        if (insert == 0) throw new OesException(ResultCodeEnum.ADD_COURSE_INFO_FAILED);

        // 添加成功
        // 要想课程表与课程描述表是一对一的关系，那么它们的主键得保持一致
        String cid = eduCourse.getId();

        // 2.向课程描述表中添加信息   课程表与课程描述表是一对一的关系
        // 创建描述对象
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        // 获得课程信息中的描述
        String description = courseInfo.getDescription();
        eduCourseDescription.setDescription(description);
        // id与课程表的一致
        eduCourseDescription.setId(cid);

        boolean save = descriptionService.save(eduCourseDescription);
        if (!save) throw new OesException(ResultCodeEnum.ADD_COURSE_DESCRIPTION_FAILED);

        return cid;
    }

    // 根据id获取课程信息
    @Transactional(rollbackFor = Exception.class) //事务注解
    @Override
    public CourseInfoVO getCourseInfoById(String courseId) {
        // 获取课程基本信息
        EduCourse eduCourse = baseMapper.selectById(courseId);
        // 如果为空则说明不存这门课程
        if (eduCourse == null) return null;
        // 将eduCourse转化为courseInfoVO
        CourseInfoVO courseInfo = new CourseInfoVO();
        BeanUtils.copyProperties(eduCourse, courseInfo);

        // 获得课程简介
        EduCourseDescription courseDescription = descriptionService.getById(courseId);
        courseInfo.setDescription(courseDescription.getDescription());
        return courseInfo;
    }

    // 根据id更新课程信息
    @Transactional(rollbackFor = Exception.class) //事务注解
    @Override
    public void updateCourseInfo(CourseInfoVO courseInfo) {
        //更新课程基本信息
        //将courseInfo转化为eduCourse
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfo, eduCourse);

        int update = baseMapper.updateById(eduCourse);
        if (update == 0) throw new OesException(ResultCodeEnum.UPDATE_COURSE_INFO_FAILED);

        //更新课程简介
        EduCourseDescription courseDescription = new EduCourseDescription();
        courseDescription.setDescription(courseInfo.getDescription());
        courseDescription.setId(courseInfo.getId());

        boolean updateDescription = descriptionService.updateById(courseDescription);

        if (!updateDescription) throw new OesException(ResultCodeEnum.UPDATE_COURSE_DESCRIPTION_FAILED);
    }

    // 根据课程id获得课程信息-讲师-章节
    @Override
    public CoursePublishVO getPublishCourseInfoById(String courseId) {
        // 调用mapper
        return baseMapper.getPublishCourseInfo(courseId);
    }

    // 根据课程id发布课程
    @Override
    public void publishCourse(String courseId) {
        EduCourse eduCourse = new EduCourse();
        eduCourse.setId(courseId);
        //设置为已发布状态
        eduCourse.setStatus("Normal");
        baseMapper.updateById(eduCourse);
    }

    // 分页条件查询
    @Override
    public void pageQuery(Page<EduCourse> coursePage, CourseQueryCondition courseQueryCondition) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        if (courseQueryCondition == null) {
            // 无条件查询
            baseMapper.selectPage(coursePage, wrapper);
            return;
        }

        //程序运行到这里表示courseQuery条件不为空
        String title = courseQueryCondition.getTitle();
        String status = courseQueryCondition.getStatus();

        //组装条件
        if (!StringUtils.isEmpty(title)) wrapper.like("title", title);
        if (!StringUtils.isEmpty(status)) wrapper.eq("status", status);

        wrapper.orderByDesc("view_count", "gmt_create");

        //根据条件进行分页查询
        baseMapper.selectPage(coursePage, wrapper);
    }

    // 根据课程id删除课程
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCourseById(String courseId) {
        // 根据课程id删除小节
        videoService.deleteVideoByCourseId(courseId);
        // 根据课程id删除章节
        chapterService.deleteChapterByCourseId(courseId);
        // 根据课程id删除描述
        descriptionService.removeById(courseId);
        // 根据课程id删除课程本身
        int delete = baseMapper.deleteById(courseId);
        // 抛异常
        if (delete == 0) throw new OesException(ResultCodeEnum.DELETE_COURSE_FAILED);
    }

    // 按column进行排序,并限制只显示limit条
    @Cacheable(value = "course", key = "'limitQuery'")
    @Override
    public List<EduCourse> limitQuery(String column, int limit) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(column);
        wrapper.last("limit " + limit);
        return baseMapper.selectList(wrapper);
    }

    // 根据讲师id查询讲师课程
    @Override
    public List<EduCourse> getTeacherCourseList(String teacherId) {
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", teacherId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Map<String, Object> frontPageQuery(Page<EduCourse> coursePage, CourseFrontQueryCondition courseFrontQueryCondition) {
        //定义查询条件wrapper
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        if (courseFrontQueryCondition != null) {
            //根据条件查询
            //一级分类
            if (!StringUtils.isEmpty(courseFrontQueryCondition.getSubjectParentId())) {
                wrapper.eq("subject_parent_id", courseFrontQueryCondition.getSubjectParentId());
            }
            //二级分类
            if (!StringUtils.isEmpty(courseFrontQueryCondition.getSubjectId())) {
                wrapper.eq("subject_id", courseFrontQueryCondition.getSubjectId());
            }
            //根据关注度、日期最新、价格排序
            if (!StringUtils.isEmpty(courseFrontQueryCondition.getBuyCountSort())) {
                //降序排序
                wrapper.orderByDesc("buy_count");
            }
            if (!StringUtils.isEmpty(courseFrontQueryCondition.getGmtCreateSort())) {
                //降序排序
                wrapper.orderByDesc("gmt_create");
            }
            if (!StringUtils.isEmpty(courseFrontQueryCondition.getPriceSort())) {
                //降序排序
                wrapper.orderByDesc("price");
            }
        }
        //查询
        baseMapper.selectPage(coursePage, wrapper);

        // 从分页对象中取出查询出来的数据并封装分页对象
        Map<String, Object> dataMap = new HashMap<>();

        //封装到map中
        dataMap.put("total", coursePage.getTotal());
        dataMap.put("records", coursePage.getRecords());
        dataMap.put("pages", coursePage.getPages());
        dataMap.put("current", coursePage.getCurrent());
        dataMap.put("size", coursePage.getSize());
        dataMap.put("hasNext", coursePage.hasNext());
        dataMap.put("hasPrevious", coursePage.hasPrevious());
        return dataMap;
    }

    // 学员端根据课程id查询课程信息
    @Override
    public CourseWebVO getFrontCourseInfoById(String courseId) {
        // 直接调用mapper
        return baseMapper.getFrontCourseInfoById(courseId);
    }

    // 管理员端获取所有课程信息
    @Override
    public List<CourseListVO> getAllCourseInfo() {
        List<EduCourse> courses = baseMapper.selectList(null);
        List<CourseListVO> courseList = new ArrayList<>();
        for (EduCourse course : courses) {
            // 根据teacherId查询讲师名称
            CourseListVO courseInfo = new CourseListVO();
            BeanUtils.copyProperties(course, courseInfo);
            courseInfo.setTeacherName(teacherService.getById(course.getTeacherId()).getName());
            courseList.add(courseInfo);
        }
        return courseList;
    }
}
