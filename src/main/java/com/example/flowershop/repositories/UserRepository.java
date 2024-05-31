package com.example.flowershop.repositories;

import com.example.flowershop.entity.User;
import com.example.flowershop.repositories.projection.OrdersOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>,
        PagingAndSortingRepository<User, Integer>,
        QuerydslPredicateExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query(value = "update user set password=:newPwd where email=:email", nativeQuery = true)
    boolean updatePwdByEmail(@Param("newPwd") String encodedNewPassword, @Param("email") String email);

    //通过电子邮箱查询订单，不包含已删除的订单
    @Query(value = "SELECT o.id AS order_id, u.id AS user_id, u.email, u.username, u.phone, o.status, o.total_amount, o.payment_type, o.receive_type, o.note, o.time " +
            "FROM `order` o JOIN user u ON u.id = o.user_id WHERE u.email=:email AND o.status not in ('paid_deleted','unpaid_deleted')", nativeQuery = true)
    Page<OrdersOnly> findOrderByEmailExcludeDeleted(@Param("email") String email, Pageable pageable);
}
