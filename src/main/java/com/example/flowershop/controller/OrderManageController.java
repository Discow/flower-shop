package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.repositories.projection.UserInfoAndOrders;
import com.example.flowershop.service.OrderManageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/")
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
    public RestBean<?> getOrders(@RequestParam(required = false) String type,
                                 @RequestParam(required = false) String orderId,
                                 @RequestParam(required = false) String username,
                                 @RequestParam(required = false) String phone,
                                 @RequestParam String page, @RequestParam String limit) {
        Page<UserInfoAndOrders> orders;
        if ("orderId".equalsIgnoreCase(type)) {
            orders = orderManageService.findOrderById(
                    Integer.valueOf(orderId),
                    Integer.valueOf(page),
                    Integer.valueOf(limit));
        } else if ("username".equalsIgnoreCase(type)) {
            orders = orderManageService.findOrderByUserName(
                    username,
                    Integer.valueOf(page),
                    Integer.valueOf(limit));
        } else if ("phone".equalsIgnoreCase(type)) {
            orders = orderManageService.findOrderByPhone(
                    phone,
                    Integer.valueOf(page),
                    Integer.valueOf(limit));
        } else {
            orders = orderManageService.findOrderAll(
                    Integer.valueOf(page),
                    Integer.valueOf(limit));
        }

        return RestBean.success(orders.getContent(), (int) orders.getTotalElements());
    }

}
