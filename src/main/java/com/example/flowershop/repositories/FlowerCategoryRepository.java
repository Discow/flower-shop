package com.example.flowershop.repositories;

import com.example.flowershop.entity.FlowerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowerCategoryRepository extends JpaRepository<FlowerCategory, Integer>,
        PagingAndSortingRepository<FlowerCategory, Integer>,
        QuerydslPredicateExecutor<FlowerCategory> {
    boolean existsByName(String name);
}
