package com.oes.edu.controller.front;


import com.oes.commonutils.Result;
import com.oes.edu.entity.vo.frontvo.CommentWebVO;
import com.oes.edu.service.EduCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Api(tags = "课程评论前台管理控制器")
@RestController
@RequestMapping("/eduservice/commentFront")
public class CommentFrontController {
    private EduCommentService commentService;

    @Autowired
    public void setCommentService(EduCommentService commentService) {
        this.commentService = commentService;
    }


    @ApiOperation(value = "分页查询评论内容")
    @GetMapping("pageQueryCommentInfo/{courseId}/{index}/{limit}")
    public Result pageQueryCommentInfo(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            @ApiParam(name = "index", value = "当前页", required = true)
            @PathVariable long index,
            @ApiParam(name = "limit", value = "最大显示数", required = true)
            @PathVariable long limit) {

        HashMap<String, Object> comments = commentService.pageQueryCommentInfo(courseId, index, limit);
        return Result.ok().data("comments", comments);
    }


    @ApiOperation(value = "添加评论信息")
    @PostMapping("addComment")
    public Result addComment(
            @ApiParam(name = "comment", value = "评论信息", required = true)
            @RequestBody CommentWebVO comment,
            @ApiParam(name = "request", value = "请求体,用于判断是否登录", required = true)
                    HttpServletRequest request) {
        boolean save = commentService.addComment(comment, request);
        return save ? Result.ok() : Result.error();
    }
}

