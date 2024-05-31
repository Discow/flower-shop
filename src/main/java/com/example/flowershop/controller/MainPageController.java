package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.service.MainPageService;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/customer/mainPage/")
public class MainPageController {
    @Resource
    MainPageService mainPageService;

    //查询商品
    @GetMapping("get-flower")
    public RestBean<?> getFlower(@RequestParam(required = false) Integer id,
                                 @RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer limit) {
        //TODO 改用QueryDSL
        if (id != null) {
            return RestBean.success(mainPageService.findById(id), 1);
        } else {
            Page<FlowerCategory> flowers = mainPageService.findCategoryAll(page, limit);
            return RestBean.success(flowers.getContent(), (int) flowers.getTotalElements());
        }
    }

    //获取随机的三个商品
    @GetMapping("get-random-three")
    public RestBean<?> getRandomThree() {
        List<Flower> flowers = mainPageService.getRandomThree();
        return RestBean.success(flowers, flowers.size());
    }
}
