package com.oes.statistics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oes.statistics.entity.StatisticsDaily;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {
    // 统计注册人数
    void registerMemberCount(String day);

    // 显示EChart数据
    Map<String, Object> showStatisticsData(String type,String begin, String end);
}
