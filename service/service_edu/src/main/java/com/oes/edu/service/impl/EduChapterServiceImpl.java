package com.oes.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.edu.entity.EduChapter;
import com.oes.edu.entity.EduVideo;
import com.oes.edu.entity.vo.ChapterVO;
import com.oes.edu.entity.vo.VideoVO;
import com.oes.edu.mapper.EduChapterMapper;
import com.oes.edu.service.EduChapterService;
import com.oes.edu.service.EduVideoService;
import com.oes.servicebase.exceptionhandler.OesException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-06-27
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    //为了得到所有小节
    private EduVideoService eduVideoService;

    //spring注入
    @Autowired
    public void setEduVideoService(EduVideoService eduVideoService) {
        this.eduVideoService = eduVideoService;
    }

    // 根据courseID获取章节以及小节的视频
    @Override
    public List<ChapterVO> getChapterVideoByCourseId(String courseId) {
        // 1.根据课程id得到所有章节
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", courseId);
        List<EduChapter> eduChapters = baseMapper.selectList(chapterQueryWrapper);

        // 2.根据课程id得到所有小节
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        List<EduVideo> eduVideos = eduVideoService.list(videoQueryWrapper);

        // 3.封装所有章节,利用Map进行优化,章节id->章节类
        Map<String, ChapterVO> chapterMap = new HashMap<String, ChapterVO>();

        // 4.遍历所有章节并转换为VO类,最后加入到Map中
        for (EduChapter eduChapter : eduChapters) {
            //得到每个章节
            //将章节转换为前端需要的VO格式
            ChapterVO chapter = new ChapterVO();
            BeanUtils.copyProperties(eduChapter, chapter);
            //加入chapterMap中
            chapterMap.put(chapter.getId(), chapter);
        }

        // 5.将video加入到chapter的children下
        for (EduVideo eduVideo : eduVideos) {
            //将eduVideo转换为VO类
            VideoVO video = new VideoVO();
            BeanUtils.copyProperties(eduVideo, video);
            //根据章节id将video加入到chapter下
            ChapterVO parent = chapterMap.get(eduVideo.getChapterId());
            parent.addChild(video);
        }

        // 6.封装最后数据并返回
        return new ArrayList<>(chapterMap.values());
    }

    // 根据chapterId删除章节
    @Override
    public boolean deleteChapterById(String chapterId) {
        //先判断章节下面是否有小节，如果有小节，不能进行删除
        //判断是否有小节，edu_video数据库中是否有章节id
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id", chapterId);

        int count = eduVideoService.count(wrapper);
        if (count > 0) throw new OesException(ResultCodeEnum.DELETE_CHAPTER_FAILED);

        //可以删除,调用mapper删除该章节
        return baseMapper.deleteById(chapterId) > 0;
    }

    // 根据课程id删除章节
    @Override
    public void deleteChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        baseMapper.delete(wrapper);
    }
}
