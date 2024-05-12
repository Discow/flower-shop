package com.example.flowershop.service;

import com.example.flowershop.dto.OrderItemDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest
@Transactional
@Commit
class CustomerOrderServiceTest {
    @Autowired
    CustomerOrderService service;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addOrder() {
        ArrayList<OrderItemDto> items = new ArrayList<>();
        items.add(new OrderItemDto(1, 1));
        items.add(new OrderItemDto(2, 1));
        System.out.println(service.addOrder(String.valueOf(3), "支付宝", "同城速递", "测试备注", items));
    }

    @Test
    void deleteOrder() {
        System.out.println(service.deleteOrder(2));
    }

    @Test
    void cancelOrder() {
        System.out.println(service.cancelOrder(2));
    }

    @Test
    void findOrderAll() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findOrderAll(String.valueOf(3), 1, 10).getContent()));
    }
}