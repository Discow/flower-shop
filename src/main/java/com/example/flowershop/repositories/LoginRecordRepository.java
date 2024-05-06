package com.example.flowershop.repositories;

import com.example.flowershop.entity.LoginRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRecordRepository extends JpaRepository<LoginRecord,Integer> {
    Page<LoginRecord> findByUserId(Integer userId, Pageable pageable);
}
