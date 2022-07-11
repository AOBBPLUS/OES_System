package com.oes.edu.service;

import com.oes.edu.entity.EduVideo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author zh
 * @since 2022-06-27
 */
public interface EduVideoService extends IService<EduVideo> {
    // 根据课程id删除小节
    void deleteVideoByCourseId(String courseId);
    // 根据id删除小节视频,底层会调用vod删除阿里云端的视频
    boolean deleteVideoById(String id);
}
