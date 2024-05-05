package com.example.flowershop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logistics {
    private Integer id;
    private String company;
    private String consignor;
    private String receiver;
    private String address;
    private Date estimated_time; //预计送达时间
    private Order order; //订单-物流，一对一关系
    private List<LogisticsTimeline> logisticsTimelines; //物流-物流时间线，一对多关系
}
