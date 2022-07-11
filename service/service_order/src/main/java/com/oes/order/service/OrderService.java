package com.oes.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author oes
 * @since 2022-07-04
 */
public interface OrderService extends IService<Order> {
    // 生成订单信息
    String generateOrder(String courseId, HttpServletRequest request);

    // 分页查询订单信息
    void pageQueryOrderInfo(Page<Order> orderPage);

    // 根据订单id查询订单
    Order getOrderInfoById(String orderId);

    // 是否购买了课程
    boolean haveBuyCourse(String courseId, String memberId);
}
