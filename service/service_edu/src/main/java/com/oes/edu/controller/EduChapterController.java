package com.oes.edu.controller;

import com.oes.commonutils.Result;
import com.oes.edu.entity.EduChapter;
import com.oes.edu.entity.vo.ChapterVO;
import com.oes.edu.service.EduChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "课程章节管理控制器")
@RestController
@RequestMapping("/eduservice/chapter")
public class EduChapterController {
    private EduChapterService eduChapterService;

    @Autowired
    public void setEduChapterService(EduChapterService eduChapterService) {
        this.eduChapterService = eduChapterService;
    }

    @ApiOperation(value = "根据课程id获取课程大纲列表")
    @GetMapping("getChapterVideoInfo/{courseId}")
    public Result getChapterVideoInfo(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId) {
        List<ChapterVO> list = eduChapterService.getChapterVideoByCourseId(courseId);
        return Result.ok().data("allChapterVideo", list);
    }

    @ApiOperation(value = "添加章节")
    @PostMapping("addChapterInfo")
    public Result addChapterInfo(@RequestBody EduChapter chapter) {
        eduChapterService.save(chapter);
        return Result.ok();
    }

    @ApiOperation(value = "根据章节id获得章节信息")
    @GetMapping("/getChapterInfo/{chapterId}")
    public Result getChapterInfo(
            @ApiParam(name = "chapterId", value = "章节id", required = true)
            @PathVariable String chapterId) {
        EduChapter eduChapter = eduChapterService.getById(chapterId);
        return Result.ok().data("chapter", eduChapter);
    }

    @ApiOperation(value = "更新章节信息")
    @PostMapping("updateChapterInfo")
    public Result updateChapterInfo(
            @ApiParam(name = "eduChapter", value = "章节信息实体", required = true)
            @RequestBody EduChapter eduChapter) {
        eduChapterService.updateById(eduChapter);
        return Result.ok();
    }

    @ApiOperation(value = "删除章节,若章节下面有小节,则不删除")
    @DeleteMapping("updateChapterInfo/{chapterId}")
    public Result deleteChapter(
            @ApiParam(name = "chapterId", value = "要删除的章节的id", required = true)
            @PathVariable String chapterId) {
        boolean flag = eduChapterService.deleteChapterById(chapterId);
        return flag ? Result.ok() : Result.error();
    }
}

