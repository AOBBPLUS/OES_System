package com.oes.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.commonutils.Result;
import com.oes.order.entity.Order;
import com.oes.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author oes
 * @since 2022-07-04
 */
@Api(tags = "用户购买订单管理控制器")
@RestController
@RequestMapping("/orderservice/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @ApiOperation(value = "生成订单,需要课程信息和用户信息")
    @PostMapping("generateOrder/{courseId}")
    public Result generateOrder(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            @ApiParam(name = "request", value = "带有用户cookie信息的请求", required = true)
                    HttpServletRequest request) {
        // 生成订单id
        String orderId = orderService.generateOrder(courseId, request);
        return Result.ok().data("orderId", orderId);
    }

    @ApiOperation(value = "分页查询订单信息")
    @GetMapping("pageQueryOrderInfo/{index}/{limit}")
    public Result pageQueryOrderInfo(
            @ApiParam(name = "index", value = "当前页", required = true)
            @PathVariable long index,
            @ApiParam(name = "limit", value = "每页查询数", required = true)
            @PathVariable long limit) {
        Page<Order> orderPage = new Page<>(index, limit);
        orderService.pageQueryOrderInfo(orderPage);
        return Result.ok().data("orders", orderPage.getRecords()).data("total", orderPage.getTotal());
    }

    @ApiOperation(value = "根据订单id,查询订单信息")
    @GetMapping("getOrderInfo/{orderId}")
    public Result getOrderInfo(
            @ApiParam(name = "courseId", value = "订单id", required = true)
            @PathVariable String orderId) {
        Order order = orderService.getOrderInfoById(orderId);
        return Result.ok().data("order", order);
    }

    @ApiOperation(value = "远程调用方法,前端不要直接使用,根据课程id和用户id查询用户是否购买课程")
    @GetMapping("remoteGetIsBuyCourse/{courseId}/{memberId}")
    public boolean remoteGetIsBuyCourse(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,
            @ApiParam(name = "memberId", value = "用户id", required = true)
            @PathVariable String memberId) {
        return orderService.haveBuyCourse(courseId, memberId);
    }
}

