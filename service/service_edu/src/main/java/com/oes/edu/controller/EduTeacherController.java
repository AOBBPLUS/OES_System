package com.oes.edu.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.commonutils.Result;
import com.oes.edu.entity.EduTeacher;
import com.oes.edu.entity.vo.TeacherQueryCondition;
import com.oes.edu.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "讲师管理控制器")
@RestController
@RequestMapping("/eduservice/teacher") //映射的请求路径
public class EduTeacherController {
    private EduTeacherService eduTeacherService;

    // 推荐使用setter注入
    @Autowired
    public void setEduTeacherService(EduTeacherService eduTeacherService) {
        this.eduTeacherService = eduTeacherService;
    }

    @ApiOperation(value = "获取教师的全部信息")
    @GetMapping("getAllTeacherInfo")
    public Result getAllTeacherInfo() {
        List<EduTeacher> list = eduTeacherService.list(null);
        return Result.ok().data("list", list);
    }

    // 逻辑删除讲师
    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("deleteTeacherInfo/{teacherId}")
    public Result deleteTeacherInfo(
            @ApiParam(name = "teacherId", value = "讲师id", required = true)
            @PathVariable String teacherId) {
        //@DeleteMapping("{id}")表示id需要通过路径传递参数
        //@PathVariable表示获取路径传入的参数值
        return eduTeacherService.removeById(teacherId) ? Result.ok() : Result.error();
    }

    // 多条件组合分页查询,由于条件为可选,因此也可以直接作为分页查询的代码
    @ApiOperation(value = "多条件组合分页查询带分页讲师列表")
    @PostMapping("pageQueryTeacherInfo/{index}/{limit}")
    public Result pageQueryTeacherByCondition(
            @ApiParam(name = "index", value = "当前页", required = true)
            @PathVariable long index,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable long limit,
            @ApiParam(name = "teacherQueryCondition", value = "查询条件", required = false)
            @RequestBody(required = false) TeacherQueryCondition teacherQueryCondition) {

        // 创page对象
        Page<EduTeacher> teacherPage = new Page<>(index, limit);
        // 分页查询,结果被封装在teacherPage对象中
        eduTeacherService.pageQueryByCondition(teacherPage, teacherQueryCondition);
        // 返回数据
        return Result.ok().data("total", teacherPage.getTotal()).data("teachers", teacherPage.getRecords());
    }

    //添加讲师
    @ApiOperation(value = "添加讲师")
    @PostMapping("addTeacherInfo")
    public Result addTeacherInfo(
            @ApiParam(name = "teacher", value = "要添加的讲师信息", required = true)
            @RequestBody(required = true) EduTeacher teacher) {
        return eduTeacherService.save(teacher) ? Result.ok() : Result.error();
    }

    //根据id查询讲师
    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getTeacherInfo/{teacherId}")
    public Result getTeacherInfoById(
            @ApiParam(name = "teacherId", value = "讲师id", required = true)
            @PathVariable String teacherId) {
        EduTeacher eduTeacher = eduTeacherService.getById(teacherId);
        return Result.ok().data("teacher", eduTeacher);
    }

    //更新讲师
    @ApiOperation(value = "更新讲师")
    @PostMapping("updateTeacherInfo")
    public Result updateTeacherInfo(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher) {
        return eduTeacherService.updateById(teacher) ? Result.ok() : Result.error();
    }
}

