package com.example.flowershop.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`comment`")
public class Comment {
    @EmbeddedId
    private CommentPK id; // 使用嵌入式主键
    private String content; //评论内容
    private Integer rating; //评价等级（1星到5星）
    private Date time; //评论时间

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "flower_id", insertable = false, updatable = false)
    private Flower flower;

    //定义联合主键
    @Data
    @Embeddable
    public static class CommentPK implements Serializable {
        @Column(name = "user_id")
        private Integer userId;
        @Column(name = "flower_id")
        private Integer flowerId;
    }
}
