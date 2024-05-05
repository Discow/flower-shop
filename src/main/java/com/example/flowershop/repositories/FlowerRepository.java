package com.example.flowershop.repositories;

import com.example.flowershop.entity.Flower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlowerRepository extends JpaRepository<Flower,Integer> {
    boolean existsByName(String name);

    Optional<Flower> findByName(String name);

    List<Flower> findByNameLike(String name);

    Page<Flower> findAll(Pageable pageable);
}
