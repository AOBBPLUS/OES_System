package com.oes.statistics.client;

import com.oes.commonutils.Result;
import org.springframework.stereotype.Component;

@Component
public class UcenterClientHystrix implements UcenterClient {
    @Override
    public Result remoteRegisterCount(String day) {
        return Result.error().message("生成数据失败");
    }
}
