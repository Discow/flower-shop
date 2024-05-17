package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.service.FlowerManageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商城主页控制器，负责展示搜索商品
 */
@RestController
@RequestMapping("/api/customer/")
public class MainPageController {
    @Resource
    FlowerManageService flowerManageService;

    //查询商品
    @GetMapping("get-flower")
    public RestBean<?> getFlower(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String categoryName,
                                 String page, String limit) {
        if (name != null) { //按商品名查询
            List<Flower> flowers = flowerManageService.findFlowerByNameLike(name);
            return RestBean.success(flowers, flowers.size());
        } else if (categoryName != null) { //按分类名查询
            Page<Flower> flowers = flowerManageService.findByCategoryName(categoryName, Integer.valueOf(page), Integer.valueOf(limit));
            return RestBean.success(flowers.getContent(), (int) flowers.getTotalElements());
        } else { //查询全部
            Page<FlowerCategory> flowers = flowerManageService.findCategoryAll(
                    Integer.valueOf(page),
                    Integer.valueOf(limit));
            return RestBean.success(flowers.getContent(), (int) flowers.getTotalElements());
        }
    }

    //获取最新上架的商品
    @GetMapping("get-latest-flower")
    public RestBean<?> getLatest(String page, String limit) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(limit), Sort.by("addTime").descending());
        Page<Flower> flowers = flowerManageService.findByStatus("已上架", pageable);
        return RestBean.success(flowers.getContent(), (int) flowers.getTotalElements());
    }
}
