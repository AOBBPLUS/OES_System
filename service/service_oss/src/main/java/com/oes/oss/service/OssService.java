package com.oes.oss.service;

import org.springframework.web.multipart.MultipartFile;

/*
 * OSS服务类
 */
public interface OssService {
    /*
     * 文件上传到阿里云oss
     */
    String uploadAvatarFile(MultipartFile file);
}
