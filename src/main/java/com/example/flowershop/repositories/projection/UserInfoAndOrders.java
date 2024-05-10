package com.example.flowershop.repositories.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("all")
public interface UserInfoAndOrders {
    @Value("#{target.user_id}")
    Integer getUserId();
    String getEmail();
    String getUsername();
    String getPhone();
    @Value("#{target.order_id}")
    Integer getId();
    String getStatus();
    @Value("#{target.total_amount}")
    BigDecimal getTotalAmount();
    @Value("#{target.payment_type}")
    String getPaymentType();
    @Value("#{target.receive_type}")
    String getReceiveType();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date getTime();
}
