package com.oes.order.controller;


import com.oes.commonutils.Result;
import com.oes.order.service.PayLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "支付日志表管理控制器")
@RestController
@RequestMapping("/orderservice/paylog")
public class PayLogController {
    private PayLogService payLogService;

    @Autowired
    public void setPayLogService(PayLogService payLogService) {
        this.payLogService = payLogService;
    }

    @ApiOperation(value = "根据订单id生成微信支付二维码")
    @GetMapping("generateOrCode/{orderNo}")
    public Result generateOrCode(
            @ApiParam(name = "orderNo", value = "订单id", required = true)
            @PathVariable String orderNo) {
        Map<String, Object> OrCode = payLogService.generateOrCode(orderNo);
        return Result.ok().data("OrCode", OrCode);
    }

    @ApiOperation(value = "根据订单id查询支付状态")
    @GetMapping("queryOrderState/{orderNo}")
    public Result queryOrderState(
            @ApiParam(name = "orderNo", value = "订单id", required = true)
            @PathVariable String orderNo) {
        Map<String, String> stateMap = payLogService.queryOrderStatus(orderNo);
        if (stateMap == null) {
            return Result.error().message("支付出错");
        }
        if (stateMap.get("trade_state").equals("SUCCESS")) {
            // 更新支付表
            payLogService.updateOrderState(stateMap);
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("正在支付").code(25000);
    }
}

