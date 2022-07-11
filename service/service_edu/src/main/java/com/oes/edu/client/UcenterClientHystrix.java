package com.oes.edu.client;

import com.oes.servicebase.vo.UcenterMemberVO;
import org.springframework.stereotype.Component;

// ucenter服务熔断时使用的服务类
@Component
public class UcenterClientHystrix implements UcenterClient {
    @Override
    public UcenterMemberVO remoteGetUserInfo(String id) {
        return null;
    }
}
