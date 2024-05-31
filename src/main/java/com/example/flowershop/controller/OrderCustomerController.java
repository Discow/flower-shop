package com.example.flowershop.controller;

import com.example.flowershop.dto.OrderItemDto;
import com.example.flowershop.dto.RestBean;
import com.example.flowershop.repositories.projection.OrdersOnly;
import com.example.flowershop.service.CustomerOrderService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户订单控制器，用于顾客下单、查看订单、取消订单、确认收货等操作
 */
@RestController
@RequestMapping("/api/customer/")
public class OrderCustomerController {
    @Resource
    CustomerOrderService customerOrderService;

    //下单
    @PostMapping("add-order")
    public RestBean<?> addOrder(Authentication authentication, String paymentType, String receiveType,
                                @RequestParam(required = false) String note, @RequestBody List<OrderItemDto> items) {
        String email = authentication.getName(); //从security上下文获取当前登录的用户名
        if (customerOrderService.addOrder(email, paymentType, receiveType, note, items)) {
            return RestBean.success("下单成功");
        } else {
            return RestBean.failure("下单失败");
        }
    }

    //查看订单
    @GetMapping("get-order")
    public RestBean<?> getOrder(Authentication authentication, @RequestParam(required = false) String status,
                                Integer page, Integer limit) {
        String email = authentication.getName();
        //TODO 改用QueryDSL
        Page<OrdersOnly> orders = customerOrderService.findOrderAll(email, status, page, limit);
        return RestBean.success(orders.getContent(), (int) orders.getTotalElements());
    }

    //取消订单
    @GetMapping("cancel-order")
    public RestBean<?> cancelOrder(Authentication authentication, Integer orderId) {
        if (customerOrderService.cancelOrder(authentication.getName(), orderId)) {
            return RestBean.success("订单取消成功");
        } else {
            return RestBean.failure("订单取消失败");
        }
    }

    //删除订单
    @GetMapping("delete-order")
    public RestBean<?> deleteOrder(Authentication authentication, Integer orderId) {
        if (customerOrderService.deleteOrder(authentication.getName(), orderId)) {
            return RestBean.success("订单删除成功");
        } else {
            return RestBean.failure("订单删除失败");
        }
    }

    //确认收货
    @GetMapping("confirm-receipt")
    public RestBean<?> confirmReceipt(Authentication authentication, Integer orderId) {
        if (customerOrderService.confirmReceipt(authentication.getName(), orderId)) {
            return RestBean.success("确认收货成功");
        } else {
            return RestBean.failure("确认收货失败：当前订单已取消");
        }
    }
}
