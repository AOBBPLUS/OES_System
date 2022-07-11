package com.oes.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oes.edu.entity.EduChapter;
import com.oes.edu.entity.vo.ChapterVO;

import java.util.List;

// 课程服务类
public interface EduChapterService extends IService<EduChapter> {
    // 根据courseId获取章节以及小节的视频
    List<ChapterVO> getChapterVideoByCourseId(String courseId);

    // 根据chapterId删除章节
    boolean deleteChapterById(String chapterId);

    // 根据课程id删除章节
    void deleteChapterByCourseId(String courseId);
}
