package com.oes.statistics.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.oes.statistics.mapper")
public class StatisticsConfig {
}
