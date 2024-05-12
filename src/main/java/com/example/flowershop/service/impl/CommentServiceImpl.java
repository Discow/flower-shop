package com.example.flowershop.service.impl;

import com.example.flowershop.entity.Comment;
import com.example.flowershop.entity.User;
import com.example.flowershop.repositories.CommentRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;

@Transactional
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    UserRepository userRepository;
    @Resource
    CommentRepository commentRepository;

    @Override
    public boolean addComment(String email, Integer flowerId, Integer rating, String content) {
        //TODO 用户邮箱从在controller层从spring security获取
        User user = userRepository.findByEmail(email).orElse(null);

        try {
            if (user != null) {
                //设置联合主键
                Comment.CommentPK commentPK = new Comment.CommentPK();
                commentPK.setUserId(user.getId());
                commentPK.setFlowerId(flowerId);
                //创建评论对象
                Comment comment = new Comment();
                comment.setId(commentPK);
                comment.setContent(content);
                comment.setRating(rating);
                comment.setTime(new Date());
                //持久化操作
                commentRepository.save(comment);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public boolean removeComment(String email, Integer flowerId) {
        //TODO 用户邮箱在controller层从spring security获取
        User user = userRepository.findByEmail(email).orElse(null);

        try {
            if (user != null) {
                //设置联合主键
                Comment.CommentPK commentPK = new Comment.CommentPK();
                commentPK.setUserId(user.getId());
                commentPK.setFlowerId(flowerId);
                //持久化操作
                commentRepository.deleteById(commentPK);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
