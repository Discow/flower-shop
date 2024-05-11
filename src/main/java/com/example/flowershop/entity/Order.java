package com.example.flowershop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单表
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String status; //订单状态
    @Column(precision = 5, scale = 2) //999.99
    private BigDecimal totalAmount; //总金额
    private String paymentType; //支付方式
    private String receiveType; //收货方式
    private String note; //订单备注
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time; //下单时间

    //建立关联
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //多对一双向关联，多方维护，一方放弃维护（被维护）
    //级联删除 删除订单时删除与商品的关联（order_detail）
    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    List<OrderDetail> orderDetails;
}
