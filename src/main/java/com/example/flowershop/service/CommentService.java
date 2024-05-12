package com.example.flowershop.service;

public interface CommentService {
    /**
     * 添加商品评论
     *
     * @param email 用户邮箱
     * @param flowerId 商品编号
     * @param rating   评价等级
     * @param content  评价内容
     */
    boolean addComment(String email, Integer flowerId, Integer rating, String content);

    //删除商品评论
    boolean removeComment(String email, Integer flowerId);
}
