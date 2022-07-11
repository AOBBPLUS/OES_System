package com.oes.order.service;

import com.oes.order.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author oes
 * @since 2022-07-04
 */
public interface PayLogService extends IService<PayLog> {
    // 根据订单id生成微信支付二维码
    Map<String, Object> generateOrCode(String orderNo);

    // 查询订单支付状态
    Map<String, String> queryOrderStatus(String orderNo);

    // 更新支付状态
    void updateOrderState(Map<String, String> stateMap);
}
