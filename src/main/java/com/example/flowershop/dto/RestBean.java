package com.example.flowershop.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.Serializable;

@Data
public class RestBean<T> implements Serializable {
    private static final Integer SUCCESS_CODE = 200;
    private static final Integer FAILURE_CODE = 500;

    private Integer code;
    private String msg;
    private Integer count;
    private T data;

    //私有化构造
    private RestBean(Integer code, String msg, Integer count, T data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    //默认的成功响应
    public static <T> RestBean<T> success() {
        return new RestBean<>(SUCCESS_CODE, null, null, null);
    }

    //带消息的成功响应
    public static <T> RestBean<T> success(String msg) {
        return new RestBean<>(SUCCESS_CODE, msg, null, null);
    }

    //带消息和数据的成功响应
    public static <T> RestBean<T> success(String msg,T data) {
        return new RestBean<>(SUCCESS_CODE, msg, null, data);
    }

    //带列表长度的成功响应
    public static <T> RestBean<T> success(T data, Integer count) {
        return new RestBean<>(0, null, count, data);
    }

    //自定义的成功响应
    public static <T> RestBean<T> success(Integer code, String msg, Integer count, T data) {
        return new RestBean<>(code, msg, count, data);
    }

    //默认的失败响应
    public static <T> RestBean<T> failure(String msg) {
        return new RestBean<>(FAILURE_CODE, msg, null, null);
    }

    //自定义失败响应
    public static <T> RestBean<T> failure(Integer code, String msg) {
        return new RestBean<>(code, msg, null, null);
    }

    //将当前对象转换为JSON格式的字符串用于返回
    @SneakyThrows
    public String asJsonString() {
        ObjectMapper objectMapper = new ObjectMapper(); //用于java对象转json
        return objectMapper.writeValueAsString(this);
    }
}