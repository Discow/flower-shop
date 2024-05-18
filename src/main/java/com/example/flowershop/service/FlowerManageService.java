package com.example.flowershop.service;

import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.repositories.projection.FlowerAndCategory;
import org.springframework.data.domain.Page;

import java.util.List;

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
    //查询所有分类
    List<FlowerCategory> findCategoryAll();
    //支持分页查询全部分类（页码，每页行数）
    Page<FlowerCategory> findCategoryAll(Integer pageNo, Integer limit);

    //添加商品
    boolean addFlower(Flower flower);
    //修改商品信息
    boolean modifyFlower(Flower flower);
    //删除商品
    boolean deleteFlower(Integer flowerId);
    //查询全部商品
    List<Flower> findFlowerAll();
    //支持分页查询全部商品（页码，每页行数）
    Page<FlowerAndCategory> findFlowerAll(Integer pageNo, Integer limit);
    //模糊查询商品
    List<Flower> findFlowerByNameLike(String name);
    //按分类名查询商品
    Page<Flower> findByCategoryName(String categoryName, Integer pageNo, Integer limit);
}
