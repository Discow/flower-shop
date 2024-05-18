package com.example.flowershop.service.impl;

import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.repositories.FlowerCategoryRepository;
import com.example.flowershop.repositories.FlowerRepository;
import com.example.flowershop.service.MainPageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class MainPageServiceImpl implements MainPageService {
    @Resource
    FlowerRepository flowerRepository;
    @Resource
    FlowerCategoryRepository flowerCategoryRepository;

    @Override
    public Page<Flower> findByStatus(String status, Pageable pageable) {
        return flowerRepository.findByStatus(status, pageable);
    }

    @Override
    public Flower findById(Integer id) {
        return flowerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Flower> getRandomThree() {
        return flowerRepository.findRandomThree();
    }

    @Override
    public List<Flower> findFlowerByNameLike(String name) {
        return flowerRepository.findByNameLike("%"+name+"%");
    }

    @Override
    public Page<Flower> findByCategoryName(String categoryName, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return flowerRepository.findByCategoryName(categoryName, pageable);
    }

    @Override
    public Page<FlowerCategory> findCategoryAll(Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit); //索引号=页码-1
        return flowerCategoryRepository.findAll(pageable);
    }
}
