package com.example.flowershop.service;

import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MainPageService {
    //TODO 添加筛选，只显示已上架的商品

    //随机获取三个商品
    List<Flower> getRandomThree();
    //按商品状态查询
    Page<Flower> findByStatus(String status, Pageable pageable);
    //根据商品编号查询
    Flower findById(Integer id);
    //模糊查询商品
    List<Flower> findFlowerByNameLike(String name);
    //按分类名查询商品
    Page<Flower> findByCategoryName(String categoryName, Integer pageNo, Integer limit);
    //支持分页查询全部分类（页码，每页行数）
    Page<FlowerCategory> findCategoryAll(Integer pageNo, Integer limit);
}
