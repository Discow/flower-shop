package com.example.flowershop.repositories;

import com.example.flowershop.entity.FlowerCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlowerCategoryRepository extends JpaRepository<FlowerCategory, Integer> {
    boolean existsByName(String name);

    Optional<FlowerCategory> findByName(String name);

    Page<FlowerCategory> findAll(Pageable pageable);
}
