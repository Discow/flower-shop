package com.example.flowershop.service;

import com.example.flowershop.entity.LoginRecord;
import com.example.flowershop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

public interface AuthService extends UserDetailsService {
    //发送邮件验证码
    void sendMailVerifyCode(String mailTo, Integer type);
    //获取图形验证码
    Map<String, Object> generateShearCaptcha();
    //校验图形验证码
    boolean checkShearCaptcha(String codeKey, String userInputCaptcha);
    //校验邮件验证码
    boolean checkMailVerifyCode(String mail,String userInputVerifyCode);
    //修改密码
    void modifyPassword(String email, String password, String newPassword);
    //用户忘记密码
    void forgotPassword(String newPassword, String email);
    //用户注册
    void doRegister(String email, String password, String username, String phone);
    //获取当前的用户信息（返回值类型为User或Admin）
    User getProfile(String email);
    //记录登录历史
    void addRecord(String ipAddr, String email, String status);
    //查询登录历史
    Page<LoginRecord> findRecord(String email, Integer page, Integer limit);
}
