package com.example.flowershop.service;

import com.example.flowershop.dto.FavoriteDetailDto;
import org.springframework.data.domain.Page;

/**
 * 收藏服务接口
 */
public interface FavoriteService {
    //添加商品收藏
    boolean addFavorite(String email, Integer flowerId);

    //取消商品收藏
    boolean removeFavorite(String email, Integer flowerId);

    //查询我的收藏
    Page<FavoriteDetailDto> findMyFavorite(String email, Integer pageNo, Integer limit);
}
