package com.oes.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.oes.commonutils.Result;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.servicebase.exceptionhandler.OesException;
import com.oes.vod.service.VodService;
import com.oes.vod.utils.ConstantPropertiesUtils;
import com.oes.vod.utils.VodInitUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "阿里云视频管理控制器")
@RestController
@RequestMapping("/vodservice/video")
public class VodController {
    private VodService vodService;

    @Autowired
    public void setVodService(VodService vodService) {
        this.vodService = vodService;
    }

    @ApiOperation(value = "上传视频到云端")
    @PostMapping("uploadAliVideo")
    public Result uploadAliVideo(
            @ApiParam(name = "file", value = "要上传的视频文件", required = true)
            @RequestBody MultipartFile file) {
        String videoId = vodService.uploadAliVideo(file);
        return Result.ok().data("videoId", videoId);
    }

    @ApiOperation(value = "根据视频id删除")
    @DeleteMapping("deleteAliVideo/{id}")
    public Result deleteAliVideo(
            @ApiParam(name = "id", value = "视频id", required = true)
            @PathVariable("id") String id) {
        vodService.deleteAliVideoById(id);
        return Result.ok();
    }

    @ApiOperation(value = "根据视频id批量删除小节视频")
    @DeleteMapping("deleteBatchAliVideo")
    public Result deleteBatchAliVideo(
            @ApiParam(name = "videoSourceIdList", value = "小节", required = true)
            @RequestParam("videoSourceIdList") List<String> videoSourceIdList) {
        vodService.deleteBatchAliVideo(videoSourceIdList);
        return Result.ok();
    }

    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("getPlayAuth/{videoId}")
    public Result getPlayAuth(
            @ApiParam(name = "videoId", value = "视频id", required = true)
            @PathVariable String videoId) {
        //创建初始化对象
        DefaultAcsClient client = null;
        try {
            client = VodInitUtils.initVodClient(ConstantPropertiesUtils.ACCESS_KEY_ID, ConstantPropertiesUtils.ACCESS_KEY_SECRET);

            //创建获取凭证request和response对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            //向request设置视频id
            request.setVideoId(videoId);

            //得到凭证
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            String playAuth = response.getPlayAuth();
            return Result.ok().data("playAuth", playAuth);
        } catch (ClientException e) {
            throw new OesException(ResultCodeEnum.GET_AUTH_FAILED);
        }
    }
}
