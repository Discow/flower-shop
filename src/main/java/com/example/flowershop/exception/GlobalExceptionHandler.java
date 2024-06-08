package com.example.flowershop.exception;

import com.example.flowershop.dto.RestBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    //预料中异常，直接返回service层抛出的异常信息
    @ExceptionHandler(GeneralException.class)
    public RestBean<?> generalExceptionHandler(GeneralException e) {
        return RestBean.failure(e.getMessage());
    }

    //未验证
    @ExceptionHandler(AccessDeniedException.class)
    public RestBean<?> accessDeniedHandler(AccessDeniedException e) {
        return RestBean.failure("未验证！");
    }

    @ExceptionHandler(Exception.class)
    public RestBean<?> ExceptionHandler(Exception e) {
        e.printStackTrace();
        return RestBean.failure("未知异常");
    }
}
