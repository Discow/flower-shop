package com.example.flowershop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * 用户信息表
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String email;
    private String username;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING) //枚举类型
    private Role role;

    public enum Role {
        USER,
        ADMIN
    }

    //建立关联 用户-订单 一对多 一方放弃维护
    @OneToMany(mappedBy = "user")
    List<Order> orders;
}
