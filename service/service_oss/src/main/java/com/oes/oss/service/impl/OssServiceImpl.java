package com.oes.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.oss.service.OssService;
import com.oes.oss.utils.ConstantPropertiesUtils;
import com.oes.servicebase.exceptionhandler.OesException;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    /*
     * 上传头像文件
     */
    @Override
    public String uploadAvatarFile(MultipartFile file) {
        //获取阿里云存储相关变量
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        //返回的url
        String url = "";
        //创建oss实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //防止文件重复
            String uuid = UUID.randomUUID().toString().substring(25).replaceAll("-", "");
            //文件上传分类，根据日期分类
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //获取文件名称
            String filename = file.getOriginalFilename();
            //拼接文件名称
            filename = datePath + "/" + uuid + filename;
            //调用oss方法实现上传
            //第一个参数 BUCKET 名称
            //第二个参数 上传到oss文件路径和文件名称
            //第三个参数 上传文件输入流
            ossClient.putObject(bucketName, filename, inputStream);
            //关闭ossClient
            ossClient.shutdown();
            //把上传之后的文件路径返回
            url = "https://" + bucketName + "." + endpoint + "/" + filename;
        } catch (Exception e) {
            throw new OesException(ResultCodeEnum.FILE_UPLOAD_FAILED);
        }
        return url;
    }
}
