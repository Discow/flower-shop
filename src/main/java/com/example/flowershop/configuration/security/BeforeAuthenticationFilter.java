package com.example.flowershop.configuration.security;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.service.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录验证之前的过滤器，用于校验图形验证码及用户名和密码的合法性
 */
@Component
public class BeforeAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("POST")) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String codeKey = request.getParameter("codeKey");
            String captcha = request.getParameter("captcha");
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
//            如果isRequireCaptcha=true，必须在登录验证之前校验图形验证码
            if (isRequireCaptcha(request)) {
                //验证图形验证码
                if (!authService.checkShearCaptcha(codeKey, captcha)) {
                    writer.write(RestBean.failure("图形验证码错误").asJsonString());
                    return;
                }
            }
        }

        //TODO 校验邮件地址和密码的合法性

        filterChain.doFilter(request, response);
    }

    private Boolean isRequireCaptcha(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Boolean isRequireCaptcha = (Boolean) session.getAttribute("isRequireCaptcha");
            if (isRequireCaptcha != null && isRequireCaptcha) {
                return true;
            }
        }
        return false;
    }
}
