package com.example.flowershop.repositories;

import com.example.flowershop.entity.Logistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogisticsRepository extends JpaRepository<Logistics, Integer> {
    //通过订单号查询物流信息
    Logistics findByOrderId(Integer orderId);
}
