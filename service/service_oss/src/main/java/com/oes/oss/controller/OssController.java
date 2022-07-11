package com.oes.oss.controller;

import com.oes.commonutils.Result;
import com.oes.oss.service.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Api(tags = "阿里云OSS上传服务控制器")
@RestController
@RequestMapping("/ossservice/oss")
public class OssController {
    // oss服务类
    private OssService ossService;

    @Autowired
    public void setOssService(OssService ossService) {
        this.ossService = ossService;
    }

    @ApiOperation(value = "上传文件头像")
    @PostMapping("uploadAvatarFile")
    public Result uploadAvatarFile(@ApiParam(name = "file", value = "要上传的头像文件", required = true) MultipartFile file) {
        // url是文件的路径,表中存储的就是路径
        String url = ossService.uploadAvatarFile(file);
        return Result.ok().data("url", url);
    }
}
