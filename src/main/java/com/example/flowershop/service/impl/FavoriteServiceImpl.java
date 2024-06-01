package com.example.flowershop.service.impl;

import com.example.flowershop.dto.FavoriteDetailDto;
import com.example.flowershop.entity.Favorite;
import com.example.flowershop.entity.QFavorite;
import com.example.flowershop.entity.QFlower;
import com.example.flowershop.entity.User;
import com.example.flowershop.repositories.FavoriteRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.service.FavoriteService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

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
    @Resource
    JPAQueryFactory jpaQueryFactory;

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
    public Page<FavoriteDetailDto> findMyFavorite(String email, Integer pageNo, Integer limit) {
        User user = userRepository.findByEmail(email).orElse(null);
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        if (user == null) {
            throw new RuntimeException("用户不存在！");
        }
        //使用QueryDSL查询
        QFavorite qFavorite = QFavorite.favorite;
        QFlower qFlower = QFlower.flower;
        List<FavoriteDetailDto> results = jpaQueryFactory
                .select(Projections.constructor(FavoriteDetailDto.class,
                        qFlower.name, qFlower.price))
                .from(qFavorite, qFlower)
                .where(qFavorite.id.flowerId.eq(qFlower.id), qFavorite.id.userId.eq(user.getId()))
                .fetch();
        //查询总行数
        Long queryCount = jpaQueryFactory.select(qFavorite.count())
                .from(qFavorite, qFlower)
                .where(qFavorite.id.flowerId.eq(qFlower.id))
                .fetchOne();
        return PageableExecutionUtils.getPage(results, pageable, () -> queryCount != null ? queryCount : 0L);
    }
}
