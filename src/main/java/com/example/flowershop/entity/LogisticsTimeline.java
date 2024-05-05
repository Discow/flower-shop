package com.example.flowershop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsTimeline {
    private Integer id;
    private String status;
    private Date updateTime;
    private Logistics logistics;
}
