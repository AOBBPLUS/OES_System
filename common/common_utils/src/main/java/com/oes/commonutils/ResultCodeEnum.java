package com.oes.commonutils;

import lombok.Getter;

// API统一返回结果状态信息
@Getter
public enum ResultCodeEnum {
    FILE_UPLOAD_FAILED("文件上传失败"),

    ADD_SUBJECT_FAILED("添加科目失败"),

    ADD_COURSE_INFO_FAILED("添加课程信息失败"),
    ADD_COURSE_DESCRIPTION_FAILED("添加课程描述失败"),

    UPDATE_COURSE_INFO_FAILED("更新课程信息失败"),
    UPDATE_COURSE_DESCRIPTION_FAILED("更新课程简介失败"),

    DELETE_CHAPTER_FAILED("章节有小节,无法删除"),
    DELETE_COURSE_FAILED("删除课程失败"),

    GET_VIDEO_FAILED("获取视频失败"),
    UPLOAD_VIDEO_FAILED("视频上传失败"),
    DELETE_VIDEO_FAILED("视频删除失败"),

    SERVICE_VOD_OUTAGE("VOD服务器宕机"),
    SERVICE_OSS_OUTAGE("OSS服务器宕机"),

    PLEASE_LOGIN("请登录"),
    MOBILE_PASSWORD_CANT_NULL("手机号和密码不能为空"),
    MOBILE_INVALID_FORMAT("手机号格式错误"),
    MOBILE_NOT_REGISTERED("该手机号未注册"),
    PASSWORD_ERROR("密码不正确"),
    USER_IS_DISABLED("该用户被禁用"),
    REGISTER_FAILED("注册失败"),
    AUTHENTICATION_CODE_ERROR("验证码失效或不正确"),
    MOBILE_HAVE_REGISTERED("该手机号已经注册"),
    LOGIN_FAILED("登录失败"),
    SAVE_USER_FAILED("保存用户失败"),

    GET_AUTH_FAILED("获取视频凭证失败"),

    GENERATE_ORCODE_FAILED("生成二维码失败"),
    QUERY_ORDER_STATE_FAILED("查询订单状态失败"),
    MODIFY_PASSWORD_FAILED("修改密码失败");
    private final String message;

    ResultCodeEnum(String message) {
        this.message = message;
    }
}
