package com.example.flowershop.repositories;

import com.example.flowershop.entity.Comment;
import com.example.flowershop.repositories.projection.CommentDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Comment.CommentPK> {
    @Modifying
    @Query(value = "update `comment` set user_id = :deletedUserId WHERE user_id = :userId", nativeQuery = true)
    void transferToDeletedAccount(@Param("userId") Integer userId, @Param("deletedUserId") Integer deletedUserId);

    @Query(value = "select content,rating,time,user.username as username from comment,user where user_id=user.id and flower_id=:flowerId", nativeQuery = true)
    Page<CommentDetail> findByFlowerId(Integer flowerId, Pageable pageable);
}
