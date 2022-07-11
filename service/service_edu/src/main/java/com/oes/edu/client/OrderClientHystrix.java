package com.oes.edu.client;

import org.springframework.stereotype.Component;

// order服务熔断时使用的服务类
@Component
public class OrderClientHystrix implements OrderClient{
    @Override
    public boolean remoteGetIsBuyCourse(String courseId, String memberId) {
        return false;
    }
}
