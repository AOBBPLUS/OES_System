package com.oes.edu.client;

import com.oes.commonutils.Result;
import org.springframework.stereotype.Component;

import java.util.List;

// vod服务熔断时使用的服务类
@Component
public class VodClientHystrix implements VodClient {
    @Override
    public Result deleteAliVideo(String id) {
        return Result.error().message("删除视频失败");
    }

    @Override
    public Result deleteBatchAliVideo(List<String> videoSourceIdList) {
        return Result.error().message("批量删除视频失败");
    }
}
