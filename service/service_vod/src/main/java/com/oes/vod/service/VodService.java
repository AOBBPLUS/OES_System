package com.oes.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    // 上传视频到阿里云云端
    String uploadAliVideo(MultipartFile file);

    // 根据id删除视频
    void deleteAliVideoById(String id);

    // 删除一系列的视频,videoSourceIdList包含的是要删除的视频的id
    void deleteBatchAliVideo(List<String> videoSourceIdList);
}
