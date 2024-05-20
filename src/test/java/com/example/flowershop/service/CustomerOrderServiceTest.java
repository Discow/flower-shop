package com.example.flowershop.service;

import com.example.flowershop.dto.OrderItemDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest
@Transactional
class CustomerOrderServiceTest {
    @Autowired
    CustomerOrderService service;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addOrder() {
        ArrayList<OrderItemDto> items = new ArrayList<>();
        items.add(new OrderItemDto(1, 1));
        items.add(new OrderItemDto(2, 1));
        System.out.println(service.addOrder("test@qq.com", "支付宝", "同城速递", "测试备注", items));
    }

    @Test
    void deleteOrder() {
        System.out.println(service.deleteOrder("test@qq.com",2));
    }

    @Test
    void cancelOrder() {
        System.out.println(service.cancelOrder("test@qq.com",1));
    }

    @Test
    void findOrderAll() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findOrderAll("test@qq.com", "已支付",1, 10).getContent()));
    }
}