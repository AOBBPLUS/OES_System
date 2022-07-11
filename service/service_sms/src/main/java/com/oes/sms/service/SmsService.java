package com.oes.sms.service;

public interface SmsService {
    // 发送验证码,使用腾讯云API
    boolean sendVerificationCode(String phone, String[] params);
}
