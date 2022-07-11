package com.oes.order.client;

import com.oes.servicebase.vo.CourseVO;
import org.springframework.stereotype.Component;

//courseClient熔断回调
@Component
public class CourseClientHystrix implements CourseClient{
    @Override
    public CourseVO remoteGetCourseInfo(String courseId) {
        return null;
    }
}
