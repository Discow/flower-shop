package com.example.flowershop.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FavoriteDetailDto {
    //商品id
    private Integer id;
    //商品名
    private String name;
    //价格
    private BigDecimal price;
    //图片
    private byte[] picture;
}
