package com.oes.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.commonutils.JwtUtils;
import com.oes.order.client.CourseClient;
import com.oes.order.client.UcenterClient;
import com.oes.order.entity.Order;
import com.oes.order.mapper.OrderMapper;
import com.oes.order.service.OrderService;
import com.oes.order.utils.OrderNoUtils;
import com.oes.servicebase.vo.CourseVO;
import com.oes.servicebase.vo.UcenterMemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author oes
 * @since 2022-07-04
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    // service_ucenter模块远程调用
    private UcenterClient ucenterClient;

    // service_edu模块远程调用
    private CourseClient courseClient;

    @Autowired
    public void setUcenterClient(UcenterClient ucenterClient) {
        this.ucenterClient = ucenterClient;
    }

    @Autowired
    public void setCourseClient(CourseClient courseClient) {
        this.courseClient = courseClient;
    }

    // 分页查询订单信息
    @Override
    public void pageQueryOrderInfo(Page<Order> orderPage) {
        baseMapper.selectPage(orderPage, null);
    }

    // 生成订单
    @Override
    public String generateOrder(String courseId, HttpServletRequest request) {
        //通过课程id远程获取课程信息
        CourseVO courseVo = courseClient.remoteGetCourseInfo(courseId);

        //课程用户id远程获取用户信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        UcenterMemberVO memberVo = ucenterClient.remoteGetUserInfo(memberId);

        //创建order对象，向order对象里面设置需要的数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.generateOrderNo());
        order.setCourseCover(courseVo.getCover());
        order.setCourseId(courseId);
        order.setCourseTitle(courseVo.getTitle());
        order.setTeacherName(courseVo.getTeacherName());
        order.setMemberId(memberId);
        order.setMobile(memberVo.getMobile());
        order.setNickname(memberVo.getNickname());
        order.setPayType(1);  //订单状态(0:未支付，1:已支付)
        order.setStatus(0);   // 支付类型 微信: 1

        baseMapper.insert(order);

        //返回订单id
        return order.getOrderNo();
    }

    // 根据订单id,查询订单信息
    @Override
    public Order getOrderInfoById(String orderId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", orderId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public boolean haveBuyCourse(String courseId, String memberId) {
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        wrapper.eq("member_id", memberId);
        wrapper.eq("status", 1);
        int count = this.count(wrapper);
        return count > 0;
    }
}
