package com.example.flowershop.service;

import com.example.flowershop.dto.CommentDetailDto;
import org.springframework.data.domain.Page;

public interface CommentService {
    /**
     * 添加商品评论
     *
     * @param email 用户邮箱
     * @param flowerId 商品编号
     * @param rating   评价等级
     * @param content  评价内容
     */
    void addComment(String email, Integer flowerId, Integer rating, String content);

    //删除商品评论
    void removeComment(String email, Integer flowerId);

    //根据商品编号查询评论
    Page<CommentDetailDto> findByFlowerId(Integer flowerId, Integer pageNo, Integer limit);
}
