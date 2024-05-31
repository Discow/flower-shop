package com.example.flowershop.service;

import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MainPageService {
    //随机获取三个商品
    List<Flower> getRandomThree();
    //根据商品编号查询
    Flower findById(Integer id);
    //支持分页查询全部分类（页码，每页行数）
    Page<FlowerCategory> findCategoryAll(Integer pageNo, Integer limit);
}
