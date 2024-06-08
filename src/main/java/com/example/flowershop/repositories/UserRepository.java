package com.example.flowershop.repositories;

import com.example.flowershop.entity.User;
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
    void updatePwdByEmail(@Param("newPwd") String encodedNewPassword, @Param("email") String email);
}
