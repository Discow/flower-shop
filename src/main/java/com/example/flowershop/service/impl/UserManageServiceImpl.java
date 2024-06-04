package com.example.flowershop.service.impl;

import com.example.flowershop.entity.QUser;
import com.example.flowershop.entity.User;
import com.example.flowershop.exception.GeneralException;
import com.example.flowershop.repositories.CommentRepository;
import com.example.flowershop.repositories.FavoriteRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.service.UserManageService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class UserManageServiceImpl implements UserManageService {
    @Resource
    UserRepository userRepository;
    @Resource
    FavoriteRepository favoriteRepository;
    @Resource
    CommentRepository commentRepository;

    //已删除的用户通用id
    private static final Integer DELETED_USER_ID = -1;

    @Override
    public void addUser(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
        } else {
            throw new GeneralException("该用户已存在！");
        }
    }

    @Override
    public void modifyUser(User newUser) {
        User user = userRepository.findByEmail(newUser.getEmail()).orElse(null);
        if (user != null) {
            newUser.setId(user.getId());
            newUser.setPassword(user.getPassword());
            userRepository.save(newUser);
        } else {
            throw new GeneralException("无法修改：该用户不存在！");
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        //先转移收藏和评论记录到专用的“已删除用户”
        transferRecordToDeletedAccount(userId);

        //级联删除该用户关联的订单和登录记录
        userRepository.deleteById(userId);
    }

    @Override
    public Page<User> findUser(Integer pageNo, Integer limit, String username, String email, String role) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        //使用QueryDSL动态查询
        QUser qUser = QUser.user;
        //构建动态查询条件
        BooleanBuilder builder = new BooleanBuilder();
        if (username != null && !username.isEmpty()) {
            builder.and(qUser.username.containsIgnoreCase(username));
        }
        if (email != null && !email.isEmpty()) {
            builder.and(qUser.email.containsIgnoreCase(email));
        }
        if (role != null && !role.isEmpty()) {
            builder.and(qUser.role.eq(User.Role.valueOf(role)));
        }
        //单表查询所有字段的动态查询可借助jpa的findAll方法
        return userRepository.findAll(builder, pageable);
    }

    // 将指定用户的收藏和评论记录转移到通用账户
    public void transferRecordToDeletedAccount(Integer userId) {
        favoriteRepository.transferToDeletedAccount(userId, DELETED_USER_ID);
        commentRepository.transferToDeletedAccount(userId, DELETED_USER_ID);
    }
}
