package com.example.flowershop.repositories;

import com.example.flowershop.entity.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Integer>, QuerydslPredicateExecutor<Flower> {
    boolean existsByName(String name);

    Optional<Flower> findByName(String name);

    @Query(value = "select price from flower where id=:id", nativeQuery = true)
    BigDecimal findPriceById(@Param("id") Integer id);

    @Query(value = "select * from flower where (status <> '已下架') ORDER BY rand() LIMIT 3", nativeQuery = true)
    List<Flower> findRandomThree();

    //更新库存
    @Modifying
    @Query(value = "update flower set inventory=:inventory where id=:id", nativeQuery = true)
    void updateInventory(Integer id, Integer inventory);
}
