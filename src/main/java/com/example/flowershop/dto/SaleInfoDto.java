package com.example.flowershop.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * 销售信息传输对象
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SaleInfoDto {
    private Long totalOrders; //总订单数
    private BigDecimal turnover; //总营业额
    private Long totalFlower; //总商品数量
    private Long pendingOrders; //待处理订单
}
