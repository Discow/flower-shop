package com.example.flowershop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Integer id;
    private String content;
    private Integer rating;
    private Date time;
    private Integer userId;
    private Integer flowerId;
    private User user; //用户-评论，一对多关系 反向引用
    private Flower flower; //鲜花-评论，一对多关系
}
