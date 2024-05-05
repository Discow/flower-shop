package com.example.flowershop.service.impl;

import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.repositories.FlowerCategoryRepository;
import com.example.flowershop.repositories.FlowerRepository;
import com.example.flowershop.service.FlowerManageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 商品服务，负责商品分类、上架
 */
@Transactional
@Service
public class FlowerManageServiceImpl implements FlowerManageService {
    @Resource
    FlowerRepository flowerRepository;
    @Resource
    FlowerCategoryRepository flowerCategoryRepository;

    @Override
    public boolean addCategory(FlowerCategory flowerCategory) {
        if (!flowerCategoryRepository.existsByName(flowerCategory.getName())) {
            flowerCategoryRepository.save(flowerCategory);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean modifyCategory(FlowerCategory newFlowerCategory) {
        FlowerCategory category = flowerCategoryRepository.findByName(newFlowerCategory.getName()).orElse(null);
        if (category != null) {
            newFlowerCategory.setId(category.getId());
            flowerCategoryRepository.save(newFlowerCategory);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteCategory(Integer categoryId) {
        try {
            flowerCategoryRepository.deleteById(categoryId);
            return true;
        } catch (Exception e) {
            //出现异常执行回滚操作
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public List<FlowerCategory> findCategoryAll() {
        return flowerCategoryRepository.findAll();
    }

    @Override
    public List<FlowerCategory> findCategoryAll(Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit); //索引号=页码-1
        return flowerCategoryRepository.findAll(pageable).getContent();
    }

    @Override
    public boolean addFlower(Flower flower) {
        flower.setAddTime(new Date());
        if (!flowerRepository.existsByName(flower.getName())) {
            flowerRepository.save(flower);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean modifyFlower(Flower newFlower) {
        newFlower.setAddTime(new Date());
        //按商品名查询Id
        Flower flower = flowerRepository.findByName(newFlower.getName()).orElse(null);
        if (flower != null) {
            newFlower.setId(flower.getId());
            flowerRepository.save(newFlower);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteFlower(Integer flowerId) {
        try {
            flowerRepository.deleteById(flowerId);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public List<Flower> findFlowerAll() {
        return flowerRepository.findAll();
    }

    @Override
    public List<Flower> findFlowerAll(Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit); //索引号=页码-1
        return flowerRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Flower> findFlowerByNameLike(String name) {
        return flowerRepository.findByNameLike("%"+name+"%");
    }
}
