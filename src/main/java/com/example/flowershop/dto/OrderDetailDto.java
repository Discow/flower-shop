package com.example.flowershop.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * 订单详情的投影
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailDto {
    private Integer id; //商品编号
    private String name; //商品名
    private BigDecimal price; //单价
    private byte[] picture; //图片
    private Integer quantity; //购买数量
    private Integer rating; //评分
    private String content; //评价内容
}
