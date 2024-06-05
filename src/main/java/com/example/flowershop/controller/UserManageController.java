package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.User;
import com.example.flowershop.service.UserManageService;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/admin/")
public class UserManageController {
    @Resource
    UserManageService userManageService;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("add-user")
    public RestBean<?> addUser(String email, String username, String password,
                               String phone, String role) {
        User user = saveUser(null, email, username, password, phone, role);
        userManageService.addUser(user);
        return RestBean.success("添加用户成功");
    }

    @PostMapping("modify-user")
    public RestBean<?> modifyUser(Integer id, String email, String username, @RequestParam(required = false) String password,
                                  String phone, String role) {
        User user = saveUser(id, email, username, password, phone, role);
        userManageService.modifyUser(user);
        return RestBean.success("修改用户成功");
    }

    @GetMapping("delete-user")
    public RestBean<?> deleteUser(Integer userId) {
        if (userId == -1) {
            return RestBean.failure("系统保留账户禁止删除");
        }
        userManageService.deleteUser(userId);
        return RestBean.success("删除用户成功");
    }

    @GetMapping("get-user")
    public RestBean<?> getUser(Integer page, Integer limit,
                               @RequestParam(required = false) String username,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String role) {
        Page<User> users = userManageService.findUser(page, limit, username, email, role);
        return RestBean.success(users.getContent(), (int) users.getTotalElements());
    }

    private User saveUser(Integer id, String email, String username, String password,
                          String phone, String role) {
        User.Role roleEnum = null;
        if ("user".equalsIgnoreCase(role)) {
            roleEnum = User.Role.USER;
        } else if ("admin".equalsIgnoreCase(role)) {
            roleEnum = User.Role.ADMIN;
        }
        if (password != null) {
            password = encoder.encode(password);
        }
        return User.builder()
                .id(id)
                .email(email)
                .username(username)
                .password(password)
                .phone(phone)
                .role(roleEnum)
                .build();
    }
}
