package com.example.flowershop.service;

import com.example.flowershop.dto.FavoriteDetailDto;
import org.springframework.data.domain.Page;

/**
 * 收藏服务接口
 */
public interface FavoriteService {
    //添加与移除商品收藏
    String addOrRmFavorite(String email, Integer flowerId);

    //查询我的收藏
    Page<FavoriteDetailDto> findMyFavorite(String email, Integer pageNo, Integer limit);
}
