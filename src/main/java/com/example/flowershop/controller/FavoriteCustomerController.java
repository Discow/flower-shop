package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.repositories.projection.FavoriteDetail;
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

    //添加收藏
    @GetMapping("add-favorite")
    public RestBean<?> addFavorite(Authentication authentication, Integer flowerId) {
        if (favoriteService.addFavorite(authentication.getName(), flowerId)) {
            return RestBean.success();
        } else {
            return RestBean.failure("添加收藏失败");
        }
    }

    //取消收藏
    @GetMapping("remove-favorite")
    public RestBean<?> removeFavorite(Authentication authentication, Integer flowerId) {
        if (favoriteService.removeFavorite(authentication.getName(), flowerId)) {
            return RestBean.success();
        } else {
            return RestBean.failure("取消收藏失败");
        }
    }

    //查看我的收藏
    @GetMapping("get-favorite")
    public RestBean<?> getFavorite(Authentication authentication, Integer page, Integer limit) {
        Page<FavoriteDetail> myFavorite = favoriteService.findMyFavorite(authentication.getName(), page, limit);
        return RestBean.success(myFavorite.getContent(), (int) myFavorite.getTotalElements());
    }
}
