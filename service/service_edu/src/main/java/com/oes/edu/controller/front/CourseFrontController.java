package com.oes.edu.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.commonutils.JwtUtils;
import com.oes.commonutils.Result;
import com.oes.edu.client.OrderClient;
import com.oes.edu.entity.EduCourse;
import com.oes.edu.entity.vo.ChapterVO;
import com.oes.edu.entity.vo.frontvo.CourseFrontQueryCondition;
import com.oes.edu.entity.vo.frontvo.CourseWebVO;
import com.oes.edu.service.EduChapterService;
import com.oes.edu.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "课程前台显示管理控制器")
@RestController
@RequestMapping("eduservice/courseFront")
public class CourseFrontController {
    private EduCourseService courseService;

    private EduChapterService chapterService;

    private OrderClient orderClient;

    @Autowired
    public void setCourseService(EduCourseService courseService) {
        this.courseService = courseService;
    }

    @Autowired
    public void setChapterService(EduChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @Autowired
    public void setOrderClient(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @ApiOperation(value = "分页查询带条件获取课程列表")
    @PostMapping("pageQueryCourseInfo/{index}/{limit}")
    public Result pageQueryCourseInfo(
            @ApiParam(name = "index", value = "当前页", required = true)
            @PathVariable long index,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable long limit,
            @ApiParam(name = "courseFrontVo", value = "前端查询时的条件", required = false)
            @RequestBody(required = false) CourseFrontQueryCondition courseFrontQueryCondition) {
        //条件查询带分页
        Page<EduCourse> coursePage = new Page<>(index, limit);
        Map<String, Object> map = courseService.frontPageQuery(coursePage, courseFrontQueryCondition);
        return Result.ok().data("courses", map);
    }


    @ApiOperation(value = "课程详情:根据路径中课程id获取课程基本信息和课程章节信息,课程所属讲师")
    @GetMapping("getCourseInfo/{courseId}")
    public Result getFrontCourseInfoById(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            @ApiParam(name = "request", value = "请求体", required = false)
                    HttpServletRequest request) {
        // 根据课程id,编写sql语句查询课程信息
        CourseWebVO courseWebVO = courseService.getFrontCourseInfoById(courseId);

        // 根据课程id查询章节和小节
        List<ChapterVO> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        // 根据课程id和用户id查询当前课程是否已经支付
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean isBuy = false;
        // 当memberId不为null时才去远程调用,避免重复
        if (memberId != null && courseId != null)
            isBuy = orderClient.remoteGetIsBuyCourse(courseId, memberId);

        return Result.ok().data("courseWebVO", courseWebVO)
                .data("chapterVideoList", chapterVideoList)
                .data("isBuyCourse", isBuy);
    }
}