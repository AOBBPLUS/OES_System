package com.oes.statistics.controller;


import com.oes.commonutils.Result;
import com.oes.statistics.service.StatisticsDailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;


@Api(tags = "数据统计管理控制器")
@RestController
@RequestMapping("/statisticsservice/statistics")
public class StatisticsDailyController {
    private StatisticsDailyService statisticsDailyService;

    // 使用setter注入
    @Autowired
    public void setStatisticsDailyService(StatisticsDailyService statisticsDailyService) {
        this.statisticsDailyService = statisticsDailyService;
    }

    @ApiOperation(value = "统计注册人数")
    @PostMapping("registerMemberCount/{day}")
    public Result registerMemberCount(@PathVariable String day) {
        if (new Date(day).compareTo(new Date()) > 0) {
            return Result.error().message("生成数据日期不能超过当前日期");
        }
        statisticsDailyService.registerMemberCount(day);
        return Result.ok();
    }

    @ApiOperation(value = "显示统计数据的方法")
    @GetMapping("getStatisticsData/{type}/{begin}/{end}")
    public Result getStatisticsData(
            @ApiParam(name = "type", value = "要显示的类型", required = true)
            @PathVariable String type,
            @ApiParam(name = "begin", value = "开始日期", required = true)
            @PathVariable String begin,
            @ApiParam(name = "end", value = "结束日期", required = true)
            @PathVariable String end) {
        if (begin == null || end == null) {
            return Result.error().message("请输入日期");
        }
        // 在前端统计图的折线图中，需要返回两个json数组，而在java中
        // json串这些都是字符串，在java中能对应前端json数组的是List集合
        // 又要返回两个List集合，所以采用map封装两个list，最后返回
        Map<String, Object> statisticsData = statisticsDailyService.showStatisticsData(type, begin, end);
        return Result.ok().data("statisticsData", statisticsData);
    }
}

