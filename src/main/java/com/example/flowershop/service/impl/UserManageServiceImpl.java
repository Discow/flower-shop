package com.example.flowershop.service.impl;

import com.example.flowershop.entity.User;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.service.UserManageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class UserManageServiceImpl implements UserManageService {
    @Resource
    UserRepository userRepository;

    @Override
    public boolean addUser(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean modifyUser(User newUser) {
        User user = userRepository.findByEmail(newUser.getEmail()).orElse(null);
        if (user != null) {
            newUser.setId(user.getId());
            userRepository.save(newUser);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteUser(Integer userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            //出现异常执行回滚操作
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public List<User> findUserAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> findUserAll(Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findByPhone(String phone, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return userRepository.findByPhoneLike("%" + phone + "%", pageable);
    }

    @Override
    public Page<User> findByUsername(String username, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return userRepository.findByUsernameLike("%" + username + "%", pageable);
    }
}
