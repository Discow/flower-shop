package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.dto.UserInfoAndOrdersDto;
import com.example.flowershop.service.OrderManageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/admin/")
public class OrderManageController {
    @Resource
    OrderManageService orderManageService;

    @PostMapping("modify-order-status")
    public RestBean<?> modifyOrderStatus(String orderId, String status) {
        if (orderManageService.modifyOrderStatus(Integer.valueOf(orderId), status)) {
            return RestBean.success("订单状态修改成功");
        } else {
            return RestBean.failure("订单状态修改失败");
        }
    }

    @GetMapping("get-orders")
    public RestBean<?> getOrders(@RequestParam(required = false) String receiveType,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String phone,
                                 Integer page, Integer limit) {
        Page<UserInfoAndOrdersDto> orders = orderManageService.findOrder(page, limit, receiveType, status, phone);
        return RestBean.success(orders.getContent(), (int) orders.getTotalElements());
    }
}
