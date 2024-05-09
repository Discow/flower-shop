package com.example.flowershop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING) //枚举类型
    private Role role;

    public enum Role {
        USER,
        ADMIN
    }

    //建立关联 用户-订单 一对多 一方放弃维护，忽略序列化JSON以避免栈溢出
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonIgnore
    List<LoginRecord> loginRecords;
}
