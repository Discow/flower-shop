package com.example.flowershop.repositories;

import com.example.flowershop.entity.Comment;
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
}
