package com.oes.edu.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.commonutils.Result;
import com.oes.edu.entity.EduCourse;
import com.oes.edu.entity.EduTeacher;
import com.oes.edu.service.EduCourseService;
import com.oes.edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "讲师前台显示管理控制器")
@RestController
@RequestMapping("eduservice/teacherFront")
public class TeacherFrontController {
    private EduTeacherService teacherService;

    private EduCourseService courseService;

    // 推荐使用setter注入
    @Autowired
    public void setTeacherService(EduTeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Autowired
    public void setCourseService(EduCourseService courseService) {
        this.courseService = courseService;
    }

    @ApiOperation(value = "讲师分页查询")
    @GetMapping("/pageQueryTeacherInfo/{index}/{limit}")
    public Result pageQueryTeacherInfo(
            @ApiParam(name = "index", value = "当前页", required = true)
            @PathVariable long index,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable long limit) {
        // 分页对象
        Page<EduTeacher> teacherPage = new Page<>(index, limit);
        Map<String, Object> data = teacherService.frontPageQuery(teacherPage);
        return Result.ok().data("teachers", data);
    }

    @ApiOperation(value = "根据讲师id查询讲师信息,包含讲师所讲的课程")
    @GetMapping("getTeacherInfo/{teacherId}")
    public Result getTeacherInfo(
            @ApiParam(name = "teacherId", value = "讲师id", required = true)
            @PathVariable String teacherId) {
        // 根据讲师id查询讲师信息
        EduTeacher eduTeacher = teacherService.getById(teacherId);
        // 根据讲师id查询讲师所讲课程
        List<EduCourse> courseList = courseService.getTeacherCourseList(teacherId);
        return Result.ok().data("teacher", eduTeacher).data("courseList", courseList);
    }
}
