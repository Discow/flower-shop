package com.example.flowershop.controller;

import com.example.flowershop.dto.CommentDetailDto;
import com.example.flowershop.dto.RestBean;
import com.example.flowershop.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 评价控制器，用于顾客添加、查看、删除商品评价
 */
@RestController
@RequestMapping("/api/customer/")
public class CommentCustomerController {
    @Resource
    CommentService commentService;

    //添加评价
    @PostMapping("add-comment")
    public RestBean<?> addComment(Authentication authentication, Integer flowerId, Integer rating, String content) {
        if (commentService.addComment(authentication.getName(), flowerId, rating, content)) {
            return RestBean.success("评价成功");
        } else {
            return RestBean.failure("评价失败");
        }
    }

    //删除评价
    @GetMapping("remove-comment")
    public RestBean<?> removeComment(Authentication authentication, Integer flowerId) {
        if (commentService.removeComment(authentication.getName(), flowerId)) {
            return RestBean.success("删除评价成功");
        } else {
            return RestBean.failure("删除评价失败");
        }
    }

    //获取商品的评价
    @GetMapping("get-comment")
    public RestBean<?> getComment(Integer flowerId, Integer page, Integer limit) {
        Page<CommentDetailDto> comments = commentService.findByFlowerId(flowerId, page, limit);
        return RestBean.success(comments.getContent(), (int) comments.getTotalElements());
    }
}
