package com.oes.ucenter.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.oes.ucenter.mapper")
public class UcenterConfig {
}
