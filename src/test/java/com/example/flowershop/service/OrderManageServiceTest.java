package com.example.flowershop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderManageServiceTest {
    @Autowired
    OrderManageService service;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void modifyOrderStatus() {
        System.out.println(service.modifyOrderStatus(2, "已发货"));
    }

    @Test
    void findOrder() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findOrder(1, 10, null, null, null).getContent()));
    }

    @Test
    void findOrderById() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findOrderById(2,1,10).getContent()));
    }

    @Test
    void findOrderByUserName() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findOrderByUserName("test",1,10).getContent()));
    }

    @Test
    void findOrderByPhone() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findOrderByPhone("19088889999",1,10).getContent()));
    }

    // TODO
    @Test
    void findOrderByCategory() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findOrderByCategory(2,1,10).getContent()));
    }
}