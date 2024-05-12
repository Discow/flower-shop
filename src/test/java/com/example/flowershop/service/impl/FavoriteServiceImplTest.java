package com.example.flowershop.service.impl;

import com.example.flowershop.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FavoriteServiceImplTest {
    @Autowired
    FavoriteService service;

    @Test
    void addFavorite() {
        service.addFavorite(String.valueOf(3),1);
    }

    @Test
    void removeFavorite() {
        service.removeFavorite(String.valueOf(3), 1);
    }
}