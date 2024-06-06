package com.example.flowershop.service;

import com.example.flowershop.dto.CartDto;
import com.example.flowershop.dto.OrderItemDto;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    //添加商品到购物车
    void addCart(String email, OrderItemDto orderItemDto);

    //删除购物车中的商品
    void delCart(String email, Integer flowerId);

    //修改购物车
    void updateCart(String email, OrderItemDto orderItemDto);

    //查询购物车中的商品
    List<CartDto> findAll(String email);

    //购物车结算
    void buy(String email, String paymentType, String receiveType, String note);

    //获取购物车总金额
    BigDecimal getTotalAmount(String email);
}
