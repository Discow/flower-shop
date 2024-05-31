package com.example.flowershop.repositories;

import com.example.flowershop.entity.Order;
import com.example.flowershop.repositories.projection.OrdersOnly;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Modifying
    @Query(value = "update `order` set status=:status where id=:orderId", nativeQuery = true)
    void modifyOrderStatus(@Param("orderId") Integer orderId, @Param("status") String status);

    //根据订单状态查询
    @Query(value = "SELECT o.id AS order_id, u.id AS user_id, u.email, u.username, u.phone, o.status, o.total_amount, o.payment_type, o.receive_type, o.note, o.time " +
            "FROM `order` o JOIN user u ON u.id = o.user_id WHERE u.email=:email AND o.status=:status", nativeQuery = true)
    Page<OrdersOnly> findOrderByEmailAndStatus(@Param("email") String email, @Param("status") String status, Pageable pageable);
}
