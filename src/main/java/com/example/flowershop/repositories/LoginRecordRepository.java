package com.example.flowershop.repositories;

import com.example.flowershop.entity.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord,Integer> {
    List<LoginRecord> findByUserId(Integer userId);
}
