package com.oes.statistics.client;

import com.oes.commonutils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(value = "service-ucenter", fallback = UcenterClientHystrix.class)
public interface UcenterClient {
    @GetMapping("/ucenterservice/member/remoteRegisterCount/{day}")
    Result remoteRegisterCount(@PathVariable("day") String day);
}
