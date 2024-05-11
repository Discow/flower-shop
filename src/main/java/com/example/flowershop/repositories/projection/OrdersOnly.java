package com.example.flowershop.repositories.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用于仅查询用户订单的投影接口
 */
@SuppressWarnings("all")
public interface OrdersOnly {
    @Value("#{target.order_id}")
    Integer getId();
    String getStatus();
    @Value("#{target.total_amount}")
    BigDecimal getTotalAmount();
    @Value("#{target.payment_type}")
    String getPaymentType();
    @Value("#{target.receive_type}")
    String getReceiveType();
    String getNote();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date getTime();
}
