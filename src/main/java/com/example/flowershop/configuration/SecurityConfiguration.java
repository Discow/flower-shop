package com.example.flowershop.configuration;

import com.example.flowershop.configuration.security.BeforeAuthenticationFilter;
import com.example.flowershop.configuration.security.CustomAuthenticationFailureHandler;
import com.example.flowershop.configuration.security.CustomAuthenticationSuccessHandler;
import com.example.flowershop.service.AuthService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.annotation.Resource;

@Configuration
@EnableMethodSecurity //开启基于方法的授权
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Resource
    AuthService authService;
    @Resource
    BeforeAuthenticationFilter beforeAuthenticationFilter;
    @Resource
    CustomAuthenticationSuccessHandler successHandler;
    @Resource
    CustomAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login.html", "/register.html", "/forgot-password.html", "/api/auth/**").permitAll() //放行登录验证相关的页面和api
                .antMatchers("/res/**", "/favicon.ico").permitAll() //放行静态资源
                .regexMatchers("/admin[\\w\\-.]*").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/api/auth/doLogin") //默认表单提交路径"/login"，重写attemptAuthentication后该配置及以下三项配置将失效
                .usernameParameter("email")
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenRepository(new InMemoryTokenRepositoryImpl())
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(authService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    //注册登录验证之前的过滤器，用于校验图形验证码及用户名、密码的合法性
    @Bean
    public FilterRegistrationBean<BeforeAuthenticationFilter> beforeAuthenticationFilterRegistration() {
        FilterRegistrationBean<BeforeAuthenticationFilter> registration = new FilterRegistrationBean<>(beforeAuthenticationFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/api/auth/doLogin");
        return registration;
    }
}
