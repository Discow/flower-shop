package com.example.flowershop.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 商品和订单的中间表
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`order_detail`")
public class OrderDetail {
    @EmbeddedId
    private OrderDetailPK id; // 使用嵌入式主键

    private Integer quantity; //购买数量

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "flower_id", insertable = false, updatable = false)
    private Flower flower;

    //定义联合主键
    @Data
    @Embeddable
    public static class OrderDetailPK implements Serializable {
        @Column(name = "order_id")
        private Integer orderId; // Order实体的主键类型
        @Column(name = "flower_id")
        private Integer flowerId; // Flower实体的主键类型
    }
}
