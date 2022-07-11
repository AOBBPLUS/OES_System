package com.oes.edu.service;

import com.oes.edu.entity.EduComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oes.edu.entity.vo.frontvo.CommentWebVO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * <p>
 * 评论 服务类
 * </p>
 *
 * @author zh
 * @since 2022-06-30
 */
public interface EduCommentService extends IService<EduComment> {
    // 学员端根据课程id分页查询评论数据
    HashMap<String, Object> pageQueryCommentInfo(String courseId, long index, long limit);

    // 学员端添加评论
    boolean addComment(CommentWebVO comment, HttpServletRequest request);
}
