package com.oes.order.client;

import com.oes.servicebase.vo.CourseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-edu", fallback = CourseClientHystrix.class)
@Component
public interface CourseClient {
    @GetMapping("/eduservice/course/remoteGetCourseInfo/{courseId}")
    CourseVO remoteGetCourseInfo(@PathVariable("courseId") String courseId);
}
