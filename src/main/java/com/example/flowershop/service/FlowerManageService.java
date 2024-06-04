package com.example.flowershop.service;

import com.example.flowershop.dto.FlowerAndCategoryDto;
import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import org.springframework.data.domain.Page;

/**
 * 管理商品分类和商品的接口
 */
public interface FlowerManageService {
    //添加分类
    void addCategory(FlowerCategory flowerCategory);
    //修改分类
    void modifyCategory(FlowerCategory flowerCategory);
    //删除分类
    void deleteCategory(Integer categoryId);
    //支持分页动态查询分类（页码，每页行数）
    Page<FlowerCategory> findCategory(Integer pageNo, Integer limit, String name);

    //添加商品
    void addFlower(Flower flower);
    //修改商品信息
    void modifyFlower(Flower flower);
    //删除商品
    void deleteFlower(Integer flowerId);
    //支持分页动态查询商品（页码，每页行数）
    Page<FlowerAndCategoryDto> findFlower(Integer pageNo, Integer limit, String name, String status);
}
