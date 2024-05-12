package com.example.flowershop.service.impl;

import com.example.flowershop.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceImplTest {
    @Autowired
    CommentService service;

    @Test
    void addComment() {
        service.addComment(String.valueOf(3), 1, 5, "测试评论");
    }

    @Test
    void removeComment() {
        service.removeComment(String.valueOf(3), 1);
    }
}