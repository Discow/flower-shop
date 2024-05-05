package com.example.flowershop.repositories;

import com.example.flowershop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query(value = "update user set password=:newPwd where email=:email", nativeQuery = true)
    boolean updatePwdByEmail(@Param("newPwd") String encodedNewPassword, @Param("email") String email);

}
