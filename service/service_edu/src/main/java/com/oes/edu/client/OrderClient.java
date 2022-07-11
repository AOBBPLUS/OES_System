package com.oes.edu.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-order", fallback = OrderClientHystrix.class)
@Component
public interface OrderClient {
    // 远程调用方法,是否购买课程
    @GetMapping("/orderservice/order/remoteGetIsBuyCourse/{courseId}/{memberId}")
    boolean remoteGetIsBuyCourse(@PathVariable("courseId") String courseId,
                                 @PathVariable("memberId") String memberId);
}
