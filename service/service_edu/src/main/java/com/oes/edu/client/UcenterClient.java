package com.oes.edu.client;

import com.oes.servicebase.vo.UcenterMemberVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-ucenter", fallback = UcenterClientHystrix.class)
@Component
public interface UcenterClient {
    @GetMapping("/ucenterservice/member/remoteGetUserInfo/{id}")
    UcenterMemberVO remoteGetUserInfo(@PathVariable("id") String id);
}
