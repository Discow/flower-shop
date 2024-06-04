package com.example.flowershop.service;

import com.example.flowershop.entity.User;
import org.springframework.data.domain.Page;

/**
 * 提供给管理员管理订单的接口
 */
public interface UserManageService {
    //新增用户
    void addUser(User user);

    //修改用户信息
    void modifyUser(User newUser);

    //删除用户
    void deleteUser(Integer userId);

    //支持分页动态查询用户
    Page<User> findUser(Integer pageNo, Integer limit, String username, String email, String role);

}
