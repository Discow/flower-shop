package com.example.flowershop.service.impl;

import com.example.flowershop.dto.CommentDetailDto;
import com.example.flowershop.entity.Comment;
import com.example.flowershop.entity.QComment;
import com.example.flowershop.entity.QUser;
import com.example.flowershop.entity.User;
import com.example.flowershop.exception.GeneralException;
import com.example.flowershop.repositories.CommentRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.service.CommentService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    UserRepository userRepository;
    @Resource
    CommentRepository commentRepository;
    @Resource
    JPAQueryFactory jpaQueryFactory;

    @Override
    public void addComment(String email, Integer flowerId, Integer rating, String content) {
        //TODO 用户邮箱从在controller层从spring security获取
        User user = userRepository.findByEmail(email).orElse(null);

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
        } else {
            throw new GeneralException("用户不存在！");
        }
    }

    @Override
    public void removeComment(String email, Integer flowerId) {
        //TODO 用户邮箱在controller层从spring security获取
        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            //设置联合主键
            Comment.CommentPK commentPK = new Comment.CommentPK();
            commentPK.setUserId(user.getId());
            commentPK.setFlowerId(flowerId);
            //持久化操作
            commentRepository.deleteById(commentPK);
        } else {
            throw new GeneralException("用户不存在！");
        }
    }

    //根据商品id获取评价
    @Override
    public Page<CommentDetailDto> findByFlowerId(Integer flowerId, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        //使用QueryDSL查询
        QComment qComment = QComment.comment;
        QUser qUser = QUser.user;
        //构建查询语句
        List<CommentDetailDto> results = jpaQueryFactory
                .select(Projections.constructor(CommentDetailDto.class,
                        qComment.content, qComment.rating, qComment.time, qUser.username))
                .from(qComment, qUser)
                .where(qComment.user.id.eq(qUser.id), qComment.flower.id.eq(flowerId))
                .fetch();
        //查询总记录数
        Long queryCount = jpaQueryFactory.select(qComment.count())
                .from(qComment, qUser)
                .where(qComment.user.id.eq(qUser.id))
                .fetchOne();
        return PageableExecutionUtils.getPage(results, pageable, () -> queryCount != null ? queryCount : 0L);
    }
}
