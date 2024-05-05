package com.example.flowershop.repositories;

import com.example.flowershop.entity.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlowerRepository extends JpaRepository<Flower,Integer> {
    boolean existsByName(String name);

    Optional<Flower> findByName(String name);
}
