package com.example.flowershop.service;

import com.example.flowershop.dto.OrderDetailDto;
import com.example.flowershop.dto.UserInfoAndOrdersDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 用于管理员管理订单的接口
 */
public interface OrderManageService {
    //修改订单状态
    void modifyOrderStatus(Integer orderId, String status);

    //动态查询订单
    Page<UserInfoAndOrdersDto> findOrder(Integer pageNo, Integer limit, String receiveType, String status, String phone);

    //获取订单详情
    List<OrderDetailDto> findOrderDetail(Integer orderId);
}
