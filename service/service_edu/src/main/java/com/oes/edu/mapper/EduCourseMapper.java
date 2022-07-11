package com.oes.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oes.edu.entity.EduCourse;
import com.oes.edu.entity.vo.CoursePublishVO;
import com.oes.edu.entity.vo.frontvo.CourseWebVO;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author zh
 * @since 2022-06-27
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {
    // 查询课程发布需要显示的内容
    CoursePublishVO getPublishCourseInfo(String courseId);

    // 学员端根据课程id查询课程信息,包括课程下的章节和小节
    CourseWebVO getFrontCourseInfoById(String courseId);
}
