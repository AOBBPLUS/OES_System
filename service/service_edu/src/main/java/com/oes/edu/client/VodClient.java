package com.oes.edu.client;

import com.oes.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// service_vod服务的远程接口
@Component
@FeignClient(name = "service-vod", fallback = VodClientHystrix.class)
// name对应的是application.properties中的spring.application.name,fallback是服务宕机时调用的服务类
public interface VodClient {

    // 远程调用的微服务方法接口地址
    @DeleteMapping("/vodservice/video/deleteAliVideo/{id}")
    Result deleteAliVideo(@PathVariable("id") String id);

    // 远程调用service_vod中批量删除云端视频的接口方法
    @DeleteMapping("/vodservice/video/deleteBatchAliVideo")
    Result deleteBatchAliVideo(@RequestParam("videoSourceIdList") List<String> videoSourceIdList);
}