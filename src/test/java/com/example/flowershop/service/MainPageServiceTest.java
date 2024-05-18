package com.example.flowershop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MainPageServiceTest {
    @Autowired
    MainPageService service;

    @Test
    void getRandomThree() throws JsonProcessingException {
        System.out.println(service.getRandomThree());
    }
}