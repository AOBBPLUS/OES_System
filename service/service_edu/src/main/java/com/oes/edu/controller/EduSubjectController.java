package com.oes.edu.controller;

import com.oes.commonutils.Result;
import com.oes.edu.entity.subject.LevelSubject;
import com.oes.edu.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "课程科目管理控制器")
@RestController
@RequestMapping("/eduservice/subject")
public class EduSubjectController {
    private EduSubjectService eduSubjectService;

    @Autowired
    public void setEduSubjectService(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    // 添加课程分类
    @ApiOperation(value = "添加课程分类")
    @PostMapping("addSubjectInfo")
    public Result addSubjectInfo(@ApiParam(name = "file", value = "要上传的Excel文件", required = true) MultipartFile file) {
        eduSubjectService.saveSubject(file, eduSubjectService);
        return Result.ok();
    }

    // 查询所有分类,一级分类下面包含二级分类,二级还可以包含三级
    @ApiOperation(value = "查询所有分类,一级分类下面包含二级二类")
    @GetMapping("listSubjectInfo")
    public Result listSubjectInfo() {
        List<LevelSubject> list = eduSubjectService.getAllSubjectList();
        return Result.ok().data("list", list);
    }
}

