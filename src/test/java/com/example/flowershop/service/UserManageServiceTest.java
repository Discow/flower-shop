package com.example.flowershop.service;

import com.example.flowershop.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class UserManageServiceTest {
    @Resource
    UserManageService service;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addUser() {
        User user = User.builder()
                .email("test@qq.com")
                .username("test")
                .password(encoder.encode("000000"))
                .phone("19088889999")
                .role(User.Role.USER)
                .build();
        System.out.println(service.addUser(user));

    }

    @Test
    void modifyUser() {
        User user = User.builder()
                .email("test@qq.com")
                .username("test123")
                .password(encoder.encode("000000"))
                .phone("19088889999")
                .role(User.Role.USER)
                .build();
        System.out.println(service.modifyUser(user));
    }

    @Test
    void deleteUser() {
        assertTrue(service.deleteUser(3));
    }

    @Test
    void findUserAll() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findUserAll()));
    }

    @Test
    void testFindUserAll() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findUserAll(1, 10)));
    }

    @Test
    void findByPhone() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findByPhone("13", 1, 10)));
    }

    @Test
    void findByUsername() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findByUsername("test", 1, 10)));
    }
}