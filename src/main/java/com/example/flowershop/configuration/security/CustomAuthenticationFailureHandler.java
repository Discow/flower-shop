package com.example.flowershop.configuration.security;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.service.AuthService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Resource
    AuthService authService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //登录失败后设置isRequireCaptcha=true，并通知前端展示验证码
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();
        session.setAttribute("isRequireCaptcha", true);
        writer.write(RestBean.failure("用户名或密码错误").asJsonString());
        authService.addRecord(request.getRemoteAddr(), request.getParameter("email"), "登录失败");
    }
}
