package com.example.flowershop.repositories;

import com.example.flowershop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>,
        QuerydslPredicateExecutor<Order> {
    @Modifying
    @Query(value = "update `order` set status=:status where id=:orderId", nativeQuery = true)
    void modifyOrderStatus(@Param("orderId") Integer orderId, @Param("status") String status);
}
