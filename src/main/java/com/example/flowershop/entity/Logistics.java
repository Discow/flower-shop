package com.example.flowershop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 物流
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`logistics`")
public class Logistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String company;
    private String consignor;
    private String receiver;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date estimated_time; //预计送达时间

    //建立关联
    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order; //订单-物流，一对一关系

    //反向关联物流时间线
    @OneToMany(mappedBy = "logistics")
    @JsonIgnore
    private List<LogisticsTimeline> logisticsTimelines; //物流-物流时间线，一对多关系
}
