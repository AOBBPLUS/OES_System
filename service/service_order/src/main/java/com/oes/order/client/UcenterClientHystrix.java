package com.oes.order.client;

import com.oes.servicebase.vo.UcenterMemberVO;
import org.springframework.stereotype.Component;

// 熔断回调接口
@Component
public class UcenterClientHystrix implements UcenterClient{
    @Override
    public UcenterMemberVO remoteGetUserInfo(String id) {
        return null;
    }
}
