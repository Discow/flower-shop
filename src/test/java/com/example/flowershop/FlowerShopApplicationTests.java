package com.example.flowershop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class FlowerShopApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void bcryptTest() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("000000"));
    }
}
