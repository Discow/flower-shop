package com.example.flowershop.service.impl;

import com.example.flowershop.entity.Favorite;
import com.example.flowershop.entity.User;
import com.example.flowershop.repositories.FavoriteRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.repositories.projection.FavoriteDetail;
import com.example.flowershop.service.FavoriteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

/**
 * 商品收藏服务，负责收藏和取消收藏商品
 */
@Transactional
@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Resource
    UserRepository userRepository;
    @Resource
    FavoriteRepository favoriteRepository;

    @Override
    public boolean addFavorite(String email, Integer flowerId) {
        //TODO 用户邮箱在controller层从spring security获取
        User user = userRepository.findByEmail(email).orElse(null);

        try {
            if (user != null) {
                //设置联合主键
                Favorite.FavoritePK favoritePK = new Favorite.FavoritePK();
                favoritePK.setUserId(user.getId());
                favoritePK.setFlowerId(flowerId);
                Favorite favorite = new Favorite();
                favorite.setId(favoritePK);
                //持久化操作
                favoriteRepository.save(favorite);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public boolean removeFavorite(String email, Integer flowerId) {
        //TODO 用户邮箱在controller层从spring security获取
        User user = userRepository.findByEmail(email).orElse(null);

        try {
            if (user != null) {
                //设置联合主键
                Favorite.FavoritePK favoritePK = new Favorite.FavoritePK();
                favoritePK.setUserId(user.getId());
                favoritePK.setFlowerId(flowerId);
                Favorite favorite = new Favorite();
                favorite.setId(favoritePK);
                //持久化操作
                favoriteRepository.delete(favorite);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public Page<FavoriteDetail> findMyFavorite(String email, Integer pageNo, Integer limit) {
        User user = userRepository.findByEmail(email).orElse(null);
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        if (user != null) {
            return favoriteRepository.findByUserId(user.getId(), pageable);
        } else {
            return null;
        }
    }
}
