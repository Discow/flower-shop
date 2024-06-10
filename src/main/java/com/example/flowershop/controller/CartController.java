package com.example.flowershop.controller;

import com.example.flowershop.dto.CartDto;
import com.example.flowershop.dto.OrderItemDto;
import com.example.flowershop.dto.RestBean;
import com.example.flowershop.service.CartService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/customer/")
public class CartController {
    @Resource
    CartService cartService;

    @PostMapping("add-cart")
    public RestBean<?> addCart(Authentication authentication, Integer flowerId, Integer quantity) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setFlowerId(flowerId);
        orderItemDto.setQuantity(quantity);
        cartService.addCart(authentication.getName(), orderItemDto);
        return RestBean.success("加购成功");
    }

    @GetMapping("del-cart")
    public RestBean<?> delCart(Authentication authentication, Integer flowerId) {
        cartService.delCart(authentication.getName(), flowerId);
        return RestBean.success("删除成功");
    }

    @PostMapping("update-cart")
    public RestBean<?> updateCart(Authentication authentication, Integer flowerId, Integer quantity) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setFlowerId(flowerId);
        orderItemDto.setQuantity(quantity);
        cartService.updateCart(authentication.getName(), orderItemDto);
        return RestBean.success("更新成功");
    }

    @GetMapping("get-cart")
    public RestBean<?> getCart(Authentication authentication) {
        List<CartDto> cartList = cartService.findAll(authentication.getName());
        return RestBean.success(cartList, null);
    }

    @PostMapping("settle-cart")
    public RestBean<?> settleCart(Authentication authentication, String paymentType, String receiveType, String note,
                                  String receiver, String address, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date deliveryTime) {
        cartService.buy(authentication.getName(), paymentType, receiveType, note, receiver, address, deliveryTime);
        return RestBean.success("购买成功");
    }

    //获取购物车总金额
    @GetMapping("total-amount-cart")
    public RestBean<?> getTotalAmountCart(Authentication authentication) {
        BigDecimal totalAmount = cartService.getTotalAmount(authentication.getName());
        return RestBean.success(totalAmount, null);
    }
}
