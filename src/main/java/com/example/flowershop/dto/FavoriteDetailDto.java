package com.example.flowershop.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FavoriteDetailDto {
    //商品名
    private String name;
    //价格
    private BigDecimal price;
}
