package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.User;
import com.example.flowershop.service.UserManageService;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/")
public class UserManageController {
    @Resource
    UserManageService userManageService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("add-user")
    public RestBean<?> addUser(String email, String username, String password,
                               String phone, String role) {
        User user = saveUser(email, username, password, phone, role);
        if (userManageService.addUser(user)) {
            return RestBean.success("添加用户成功");
        } else {
            return RestBean.failure("添加用户失败");
        }
    }

    @PostMapping("modify-user")
    public RestBean<?> modifyUser(String email, String username, String password,
                               String phone, String role) {
        User user = saveUser(email, username, password, phone, role);
        if (userManageService.modifyUser(user)) {
            return RestBean.success("修改用户成功");
        } else {
            return RestBean.failure("修改用户失败");
        }
    }

    @GetMapping("delete-user")
    public RestBean<?> deleteUser(String userId) {
        if (userManageService.deleteUser(Integer.valueOf(userId))) {
            return RestBean.success("删除用户成功");
        } else {
            return RestBean.failure("删除用户失败");
        }
    }

    @GetMapping("get-all-user")
    public RestBean<?> getAllUser(String page, String limit) {
        Page<User> users = userManageService.findUserAll(
                Integer.valueOf(page),
                Integer.valueOf(limit));
        return RestBean.success(users.getContent(), (int) users.getTotalElements());
    }

    @GetMapping("get-user-byPhone")
    public RestBean<?> getUserByPhone(String phone, String page, String limit) {
        Page<User> users = userManageService.findByPhone(
                phone,
                Integer.valueOf(page),
                Integer.valueOf(limit));
        return RestBean.success(users.getContent(), (int) users.getTotalElements());
    }

    @GetMapping("get-user-byUsername")
    public RestBean<?> getUserByUsername(String username, String page, String limit) {
        Page<User> users = userManageService.findByUsername(
                username,
                Integer.valueOf(page),
                Integer.valueOf(limit));
        return RestBean.success(users.getContent(), (int) users.getTotalElements());
    }

    private User saveUser(String email, String username, String password,
                          String phone, String role) {
        User.Role roleEnum = null;
        if ("user".equalsIgnoreCase(role)) {
            roleEnum = User.Role.USER;
        } else if ("admin".equalsIgnoreCase(role)) {
            roleEnum = User.Role.ADMIN;
        }
        return User.builder()
                .email(email)
                .username(username)
                .password(encoder.encode(password))
                .phone(phone)
                .role(roleEnum)
                .build();
    }
}
