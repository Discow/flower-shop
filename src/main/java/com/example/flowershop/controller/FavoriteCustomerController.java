package com.example.flowershop.controller;

import com.example.flowershop.dto.FavoriteDetailDto;
import com.example.flowershop.dto.RestBean;
import com.example.flowershop.service.FavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户收藏控制器，用于添加收藏、查看收藏、取消收藏
 */
@RestController
@RequestMapping("/api/customer/")
public class FavoriteCustomerController {
    @Resource
    FavoriteService favoriteService;

    //添加与移除收藏
    @GetMapping("add-or-rm-favorite")
    public RestBean<?> addOrRmFavorite(Authentication authentication, Integer flowerId) {
        String s = favoriteService.addOrRmFavorite(authentication.getName(), flowerId);
        return RestBean.success(s);
    }

    //查看我的收藏
    @GetMapping("get-favorite")
    public RestBean<?> getFavorite(Authentication authentication, Integer page, Integer limit) {
        Page<FavoriteDetailDto> myFavorite = favoriteService.findMyFavorite(authentication.getName(), page, limit);
        return RestBean.success(myFavorite.getContent(), (int) myFavorite.getTotalElements());
    }
}
