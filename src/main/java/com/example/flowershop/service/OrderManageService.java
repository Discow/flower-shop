package com.example.flowershop.service;

import com.example.flowershop.repositories.projection.UserInfoAndOrders;
import org.springframework.data.domain.Page;

/**
 * 用于管理员管理订单的接口
 */
public interface OrderManageService {
    //修改订单状态
    boolean modifyOrderStatus(Integer orderId, String status);

    //获取所有订单
    Page<UserInfoAndOrders> findOrderAll(Integer pageNo, Integer limit);

    //根据订单号查询订单
    Page<UserInfoAndOrders> findOrderById(Integer id, Integer pageNo, Integer limit);

    //根据用户名查询订单
    Page<UserInfoAndOrders> findOrderByUserName(String username, Integer pageNo, Integer limit);

    //根据用户手机号查询订单
    Page<UserInfoAndOrders> findOrderByPhone(String phone, Integer pageNo, Integer limit);

    //根据商品类型查询订单
    Page<UserInfoAndOrders> findOrderByCategory(Integer category, Integer pageNo, Integer limit);
}
