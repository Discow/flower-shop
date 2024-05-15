package com.example.flowershop.repositories.projection;

import org.springframework.beans.factory.annotation.Value;

@SuppressWarnings("all")
public interface FavoriteDetail {
    //商品名
    @Value("#{target.name}")
    String getFlowerName();

    //价格
    @Value("#{target.price}")
    String getPrice();
}
