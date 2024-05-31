package com.example.flowershop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserInfoAndOrdersDto {
    private Integer id;
    private String email;
    private String username;
    private String phone;
    private String status;
    private BigDecimal totalAmount;
    private String paymentType;
    private String receiveType;
    private String note;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
}
