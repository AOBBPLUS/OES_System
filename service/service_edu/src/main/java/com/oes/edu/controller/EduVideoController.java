package com.oes.edu.controller;


import com.oes.commonutils.Result;
import com.oes.edu.entity.EduVideo;
import com.oes.edu.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "课程每小节的视频的控制器")
@RestController
@RequestMapping("/eduservice/video")
public class EduVideoController {
    private EduVideoService eduVideoService;

    @Autowired
    public void setEduVideoService(EduVideoService eduVideoService) {
        this.eduVideoService = eduVideoService;
    }

    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public Result addVideo(
            @ApiParam(name = "video", value = "要添加的小节视频", required = true)
            @RequestBody EduVideo video) {
        eduVideoService.save(video);
        return Result.ok();
    }

    @ApiOperation(value = "根据id删除小节视频,底层会调用vod删除阿里云端的视频")
    @DeleteMapping("deleteVideo/{videoId}")
    public Result deleteVideoById(
            @ApiParam(name = "videoId", value = "要删除的视频的id", required = true)
            @PathVariable String videoId) {
        boolean remove = eduVideoService.deleteVideoById(videoId);
        return remove ? Result.ok() : Result.error();
    }

    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideoInfo")
    public Result updateVideoInfo(
            @ApiParam(name = "video", value = "要修改的视频信息", required = true)
            @RequestBody EduVideo eduVideo) {
        boolean update = eduVideoService.updateById(eduVideo);
        return update ? Result.ok() : Result.error();
    }

    @ApiOperation(value = "根据小节id查询小节信息")
    @GetMapping("getVideoInfo/{videoId}")
    public Result getVideoInfo(
            @ApiParam(name = "videoId", value = "小节id", required = true)
            @PathVariable String videoId) {
        EduVideo video = eduVideoService.getById(videoId);
        return Result.ok().data("video", video);
    }
}

