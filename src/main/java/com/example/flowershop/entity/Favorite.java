package com.example.flowershop.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`favorite`")
public class Favorite {
    @EmbeddedId
    private FavoritePK id; // 使用嵌入式主键

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "flower_id", insertable = false, updatable = false)
    private Flower flower;

    //定义联合主键
    @Data
    @Embeddable
    public static class FavoritePK implements Serializable {
        @Column(name = "user_id")
        private Integer userId;
        @Column(name = "flower_id")
        private Integer flowerId;
    }
}
