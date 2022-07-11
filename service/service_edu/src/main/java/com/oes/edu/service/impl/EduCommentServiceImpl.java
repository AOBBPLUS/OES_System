package com.oes.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.commonutils.JwtUtils;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.edu.client.UcenterClient;
import com.oes.edu.entity.EduComment;
import com.oes.edu.entity.vo.frontvo.CommentWebVO;
import com.oes.edu.mapper.EduCommentMapper;
import com.oes.edu.service.EduCommentService;
import com.oes.servicebase.exceptionhandler.OesException;
import com.oes.servicebase.vo.UcenterMemberVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * <p>
 * 评论 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-06-30
 */
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {
    private UcenterClient ucenterClient;

    @Autowired
    public void setUcenterClient(UcenterClient ucenterClient) {
        this.ucenterClient = ucenterClient;
    }

    // 分页查询
    @Override
    public HashMap<String, Object> pageQueryCommentInfo(String courseId, long index, long limit) {

        Page<EduComment> page = new Page<>(index, limit);

        //根据课程ud查询该课程的所有评论
        QueryWrapper<EduComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId);
        //按照时间降序
        queryWrapper.orderByDesc("gmt_create");
        baseMapper.selectPage(page, queryWrapper);

        HashMap<String, Object> map = new HashMap<>();

        map.put("pages", page.getPages());
        map.put("records", page.getRecords());
        map.put("size", page.getSize());
        map.put("current", page.getCurrent());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return map;
    }

    // 添加评论
    @Override
    public boolean addComment(CommentWebVO commentWebVO, HttpServletRequest request) {
        // 先去请求头中获取有没有token
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if (StringUtils.isEmpty(memberId)) {
            throw new OesException(ResultCodeEnum.PLEASE_LOGIN);
        }

        // 如果已经登录,则生成要添加的comment实体
        EduComment comment = new EduComment();
        BeanUtils.copyProperties(commentWebVO, comment);

        // 设置comment的memberId
        comment.setMemberId(memberId);

        // 远程调用service-ucenter中的接口
        UcenterMemberVO memberInfo = ucenterClient.remoteGetUserInfo(memberId);
        if (memberInfo != null) {
            comment.setAvatar(memberInfo.getAvatar());
            comment.setNickname(memberInfo.getNickname());
            int insert = baseMapper.insert(comment);
            return insert > 0;
        }
        return false;
    }
}
