package com.example.flowershop.configuration.security;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        HttpSession session = request.getSession();
        session.setAttribute("isRequireCaptcha", false);
        writer.write(RestBean.success("登录成功",authentication.getPrincipal()).asJsonString());
        //记录登录历史
        authService.addRecord(request.getRemoteAddr(), request.getParameter("email"), "登录成功");
    }
}
