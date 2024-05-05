package com.example.flowershop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Integer flowerId; //购买的商品ID
    private Integer quantity; //购买数量
}
