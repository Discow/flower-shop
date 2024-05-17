package com.example.flowershop.repositories;

import com.example.flowershop.entity.Flower;
import com.example.flowershop.repositories.projection.FlowerAndCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlowerRepository extends JpaRepository<Flower,Integer> {
    boolean existsByName(String name);

    Optional<Flower> findByName(String name);

    List<Flower> findByNameLike(String name);

    @Query(value = "select flower.id,flower.name,price,flower.description,picture,inventory,status,add_time,category_id from flower,flower_category where flower_category.name=:categoryName", nativeQuery = true)
    Page<Flower> findByCategoryName(String categoryName, Pageable pageable);

    @Query(value = "select flower.id,flower.name,price,flower.description,picture,inventory,status,add_time,category_id,flower_category.name as category_name from flower,flower_category where category_id=flower_category.id", nativeQuery = true)
    Page<FlowerAndCategory> findFlowerAndCategoryAll(Pageable pageable);

    @Query(value = "select price from flower where id=:id", nativeQuery = true)
    BigDecimal findPriceById(@Param("id") Integer id);

    Page<Flower> findByStatus(String status, Pageable pageable);
}
