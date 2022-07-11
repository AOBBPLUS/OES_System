package com.oes.edu.controller.front;

import com.oes.commonutils.Result;
import com.oes.edu.entity.EduCourse;
import com.oes.edu.entity.EduTeacher;
import com.oes.edu.service.EduCourseService;
import com.oes.edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "前端门户页面显示管理控制器")
@RestController
@RequestMapping("eduservice/indexFront")
public class IndexFrontController {
    private EduCourseService courseService;

    private EduTeacherService teacherService;

    @Autowired
    public void setCourseService(EduCourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setTeacherService(EduTeacherService teacherService) {
        this.teacherService = teacherService;
    }

    // 查询前8条热门课程,前4个热门讲师
    @GetMapping("index")
    public Result index() {
        // 按id降序排列,取出前4个课程
        List<EduTeacher> eduTeachers = teacherService.limitQuery("id", 4);
        // 按照id降序排列,取出前8个课程
        List<EduCourse> eduCourses = courseService.limitQuery("id", 8);
        // 返回数据
        return Result.ok().data("hotCourse", eduCourses).data("hotTeacher", eduTeachers);
    }
}
