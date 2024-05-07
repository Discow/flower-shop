package com.example.flowershop.service.impl;

import com.example.flowershop.repositories.OrderRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.repositories.projection.UserInfoAndOrders;
import com.example.flowershop.service.OrderManageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;

@Transactional
@Service
public class OrderManageServiceImpl implements OrderManageService {
    @Resource
    OrderRepository orderRepository;

    @Resource
    UserRepository userRepository;

    @Override
    public boolean modifyOrderStatus(Integer orderId, String status) {
        try {
            orderRepository.modifyOrderStatus(orderId, status);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public Page<UserInfoAndOrders> findOrderAll(Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return orderRepository.findOrderAll(pageable);
    }

    @Override
    public Page<UserInfoAndOrders> findOrderById(Integer id, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return orderRepository.findOrderById(id, pageable);
    }

    @Override
    public Page<UserInfoAndOrders> findOrderByUserName(String username, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return userRepository.findOrderByUsername(username, pageable);
    }

    @Override
    public Page<UserInfoAndOrders> findOrderByPhone(String phone, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return userRepository.findOrderByPhone(phone, pageable);
    }

    //TODO
    @Override
    public Page<UserInfoAndOrders> findOrderByCategory(Integer category, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return null;
    }

}
