package com.example.flowershop.service;

import com.example.flowershop.dto.OrderItemDto;
import com.example.flowershop.repositories.projection.OrdersOnly;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 用于顾客下单、查询订单、删除订单、取消订单等操作的接口
 */
public interface CustomerOrderService {
    //下单
    boolean addOrder(String email, String paymentType, String receiveType, String note, List<OrderItemDto> items);

    //删除订单
    boolean deleteOrder(Integer orderId);

    //取消订单
    boolean cancelOrder(Integer orderId);

    //查询当前用户的全部订单（仅获取订单集合）
    Page<OrdersOnly> findOrderAll(String email, Integer pageNo, Integer limit);

}
