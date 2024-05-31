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
    boolean addCategory(FlowerCategory flowerCategory);
    //修改分类
    boolean modifyCategory(FlowerCategory flowerCategory);
    //删除分类
    boolean deleteCategory(Integer categoryId);
    //支持分页动态查询分类（页码，每页行数）
    Page<FlowerCategory> findCategory(Integer pageNo, Integer limit, String name);

    //添加商品
    boolean addFlower(Flower flower);
    //修改商品信息
    boolean modifyFlower(Flower flower);
    //删除商品
    boolean deleteFlower(Integer flowerId);
    //支持分页动态查询商品（页码，每页行数）
    Page<FlowerAndCategoryDto> findFlower(Integer pageNo, Integer limit, String name, String status);
}
