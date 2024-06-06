package com.example.flowershop.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartDto {
    private Integer flowerId; //商品编号
    private String flowerName; //商品名
    private BigDecimal price; //价格
    private byte[] picture; //图片
    private Integer quantity; //购买数量
    private Integer inventory; //库存数量
}
