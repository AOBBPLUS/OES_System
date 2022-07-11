package com.oes.statistics.schedule;

import com.oes.statistics.service.StatisticsDailyService;
import com.oes.statistics.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

// 定时任务类
@Component
public class ScheduleTask {
    private StatisticsDailyService dailyService;

    @Autowired
    public void setDailyService(StatisticsDailyService dailyService) {
        this.dailyService = dailyService;
    }

    // cron表达式,每天每隔两小时执行
    @Scheduled(cron = "0 0 0/2 * * ?")
    public void task() {
        String day = DateUtil.formatDate(new Date());
        dailyService.registerMemberCount(day);
    }
}
