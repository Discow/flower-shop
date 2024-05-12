package com.example.flowershop.service;

/**
 * 收藏服务接口
 */
public interface FavoriteService {
    //添加商品收藏
    boolean addFavorite(String email, Integer flowerId);

    //取消商品收藏
    boolean removeFavorite(String email, Integer flowerId);
}
