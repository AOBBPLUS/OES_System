package com.oes.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.commonutils.ObjectFiledIsNull;
import com.oes.commonutils.Result;
import com.oes.edu.entity.EduCourse;
import com.oes.edu.entity.vo.CourseInfoVO;
import com.oes.edu.entity.vo.CourseListVO;
import com.oes.edu.entity.vo.CoursePublishVO;
import com.oes.edu.entity.vo.CourseQueryCondition;
import com.oes.edu.entity.vo.frontvo.CourseWebVO;
import com.oes.edu.service.EduCourseService;
import com.oes.servicebase.vo.CourseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "课程信息管理控制器")
@RestController
@RequestMapping("/eduservice/course")
public class EduCourseController {
    private EduCourseService eduCourseService;

    @Autowired
    public void setEduCourseService(EduCourseService eduCourseService) {
        this.eduCourseService = eduCourseService;
    }

    @ApiOperation(value = "查询所有课程信息")
    @GetMapping("getAllCourseInfo")
    public Result getAllCourseInfo() {
        List<CourseListVO> list = eduCourseService.getAllCourseInfo();
        return Result.ok().data("list", list).data("total", list.size());
    }

    @ApiOperation(value = "添加课程基本信息")
    @PostMapping("addCourseInfo")
    public Result addCourseInfo(
            @ApiParam(name = "courseInfo", value = "课程基本信息实体", required = true)
            @RequestBody CourseInfoVO courseInfo) {
        //查询courseInfo中为空的字段,为空则不允许插入
//        if (courseInfo.getTitle().equals("")
//                || courseInfo.getSubjectId().equals("")
//                || courseInfo.getSubjectParentId().equals("")
//                || courseInfo.getDescription().equals(""))
//            return Result.error().message("课程信息不能为空");
        // 课程信息不能为空
        Result tempResult = ObjectFiledIsNull.isFiledHaveNull(courseInfo, "课程信息不能为空");
        if (!tempResult.getSuccess()) return tempResult;

        //添加课程基本信息
        String cid = eduCourseService.addCourseInfo(courseInfo);
        return Result.ok().data("courseId", cid);
    }

    @ApiOperation(value = "根据课程id获得课程基本信息")
    @GetMapping("getCourseInfo/{courseId}")
    public Result getCourseInfo(
            @ApiParam(name = "courseId", value = "要获取信息的课程的id", required = true)
            @PathVariable String courseId) {
        CourseInfoVO courseInfo = eduCourseService.getCourseInfoById(courseId);
        return courseInfo != null ? Result.ok().data("courseInfo", courseInfo) : Result.error().message("该课程不存在");
    }

    @ApiOperation(value = "更新课程基本信息,涉及两张表")
    @PostMapping("updateCourseInfo")
    public Result updateCourseInfo(
            @ApiParam(name = "courseInfo", value = "课程信息", required = true)
            @RequestBody CourseInfoVO courseInfo) {
        eduCourseService.updateCourseInfo(courseInfo);
        return Result.ok();
    }

    @ApiOperation(value = "根据id获得课程信息-讲师-章节")
    @GetMapping("getPublishCourseInfo/{courseId}")
    public Result getPublishCourseInfo(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId) {
        CoursePublishVO coursePublishVo = eduCourseService.getPublishCourseInfoById(courseId);
        return Result.ok().data("publishCourse", coursePublishVo);
    }

    @ApiOperation(value = "课程最终发布")
    @PostMapping("publishCourse/{courseId}")
    public Result publishCourse(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId) {
        eduCourseService.publishCourse(courseId);
        return Result.ok();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("pageQueryCourseInfo/{index}/{limit}")
    public Result pageQueryCourseByCondition(
            @ApiParam(name = "index", value = "当前页", required = true)
            @PathVariable long index,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable long limit,
            @ApiParam(name = "courseQueryCondition", value = "查询条件,应该为json数据", required = false)
            @RequestBody(required = false) CourseQueryCondition courseQueryCondition) {
        // 分页查询,创建分页对象
        Page<EduCourse> coursePage = new Page<>(index, limit);
        // 根据条件进行查询
        // page方法返回的分页数据封装在分页对象coursePage中
        eduCourseService.pageQuery(coursePage, courseQueryCondition);
        // 返回数据
        return Result.ok().data("total", coursePage.getTotal()).data("courses", coursePage.getRecords());
    }

    @ApiOperation(value = "根据课程id删除课程,需要依次删除章节,小节,课程描述,课程本身")
    @DeleteMapping("deleteCourse/{courseId}")
    public Result deleteCourseById(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId) {
        eduCourseService.deleteCourseById(courseId);
        return Result.ok();
    }

    // 远程方法调用：根据课程id获得课程基本信息
    @GetMapping("remoteGetCourseInfo/{courseId}")
    public CourseVO remoteGetCourseInfo(@PathVariable String courseId) {
        CourseWebVO courseWebVO = eduCourseService.getFrontCourseInfoById(courseId);
        CourseVO courseVO = new CourseVO();
        BeanUtils.copyProperties(courseWebVO, courseVO);
        return courseVO;
    }
}

