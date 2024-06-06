package com.example.flowershop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name; //鲜花名称
    @Column(precision = 5, scale = 2)
    private BigDecimal price; //单价
    private String description; //描述
    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] picture; //图片 longblob类型
    private Integer inventory; //库存数量
    private String status; //状态 上架/下架
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addTime; //添加时间

    //关联分类 多对一
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private FlowerCategory flowerCategory;

    //反向关联收藏 一对多
    @OneToMany(mappedBy = "flower", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Favorite> favorite;

    //反向关联评价 一对多
    @OneToMany(mappedBy = "flower", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Comment> comments;

}
