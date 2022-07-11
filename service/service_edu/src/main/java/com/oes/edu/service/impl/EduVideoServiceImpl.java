package com.oes.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.commonutils.Result;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.edu.client.VodClient;
import com.oes.edu.entity.EduVideo;
import com.oes.edu.mapper.EduVideoMapper;
import com.oes.edu.service.EduVideoService;
import com.oes.servicebase.exceptionhandler.OesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-06-27
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    private VodClient vodClient;

    @Autowired
    public void setVodClient(VodClient vodClient) {
        this.vodClient = vodClient;
    }

    // 根据课程id删除小节,同时要删除视频
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteVideoByCourseId(String courseId) {
        // 根据课程id查询所有的小节视频
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        // 只查询id
        wrapper.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapper);
        // 利用stream流获取id并利用id删除视频
        List<String> videoIds = eduVideoList.stream().filter(Objects::nonNull).map(EduVideo::getVideoSourceId).collect(Collectors.toList());
        // 不为空则去删除云端视频
        if (!videoIds.isEmpty()) {
            Result result = vodClient.deleteBatchAliVideo(videoIds);
            if (!result.getSuccess())
                throw new OesException(ResultCodeEnum.SERVICE_VOD_OUTAGE);
        }
        // 删除数据库中的字段
        baseMapper.delete(wrapper);
    }

    // 根据id删除小节视频,底层会调用vod删除阿里云端的视频
    @Override
    public boolean deleteVideoById(String id) {
        //根据小节id获取小节中对应的视频id
        EduVideo eduVideo = baseMapper.selectById(id);
        String videoSourceId = eduVideo.getVideoSourceId();
        //远程调用
        if (!StringUtils.isEmpty(videoSourceId)) {
            Result result = vodClient.deleteAliVideo(videoSourceId);
            if (!result.getSuccess())
                throw new OesException(ResultCodeEnum.SERVICE_VOD_OUTAGE);
        }
        //删完视频后再删除小节
        return this.removeById(id);
    }
}
