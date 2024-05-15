package com.example.flowershop.repositories;

import com.example.flowershop.entity.Favorite;
import com.example.flowershop.repositories.projection.FavoriteDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Favorite.FavoritePK> {
    @Modifying
    @Query(value = "update `favorite` set user_id = :deletedUserId WHERE user_id = :userId", nativeQuery = true)
    void transferToDeletedAccount(@Param("userId") Integer userId, @Param("deletedUserId") Integer deletedUserId);

    @Query(value = "select name,price from flower,favorite where flower.id=favorite.flower_id and favorite.user_id=:userId", nativeQuery = true)
    Page<FavoriteDetail> findByUserId(Integer userId, Pageable pageable);
}
