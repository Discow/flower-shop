package com.example.flowershop.service;

import com.example.flowershop.entity.LoginRecord;
import com.example.flowershop.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
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
    boolean modifyPassword(String email, String password, String newPassword);
    //用户忘记密码
    boolean forgotPassword(String newPassword, String email);
    //用户注册
    boolean register(String email, String password, String username, String phone);
    //获取当前的用户信息（返回值类型为User或Admin）
    User getProfile(String email);
    //记录登录历史
    void addRecord(String ipAddr, String email, String status);
    //查询登录历史
    List<LoginRecord> findRecord(String email);
}
