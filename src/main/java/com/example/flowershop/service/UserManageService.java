package com.example.flowershop.service;

import com.example.flowershop.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 提供给管理员管理订单的接口
 */
public interface UserManageService {
    //新增用户
    boolean addUser(User user);

    //修改用户信息
    boolean modifyUser(User newUser);

    //删除用户
    boolean deleteUser(Integer userId);

    //查询所有用户
    List<User> findUserAll();

    //支持分页查询所有用户
    Page<User> findUserAll(Integer pageNo, Integer limit);

    //根据手机号模糊查询
    Page<User> findByPhone(String phone, Integer pageNo, Integer limit);

    //根据用户名模糊查询
    Page<User> findByUsername(String username, Integer pageNo, Integer limit);

}
