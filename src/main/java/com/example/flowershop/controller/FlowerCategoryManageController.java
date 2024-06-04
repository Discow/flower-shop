package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.service.FlowerManageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/admin/")
public class FlowerCategoryManageController {
    @Resource
    FlowerManageService flowerManageService;

    @PostMapping("add-category")
    public RestBean<?> addCategory(String name, String description) {
        FlowerCategory category = new FlowerCategory();
        category.setName(name);
        category.setDescription(description);
        flowerManageService.addCategory(category);
        return RestBean.success("分类添加成功");
    }

    @PostMapping("modify-category")
    public RestBean<?> modifyCategory(Integer id, String name, String description) {
        FlowerCategory category = new FlowerCategory();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        flowerManageService.modifyCategory(category);
        return RestBean.success("分类修改成功");
    }

    @GetMapping("delete-category")
    public RestBean<?> deleteCategory(Integer categoryId) {
        flowerManageService.deleteCategory(categoryId);
        return RestBean.success("分类删除成功");
    }

    @GetMapping("get-category")
    public RestBean<?> getCategory(Integer page, Integer limit, @RequestParam(required = false) String name) {
        Page<FlowerCategory> categories = flowerManageService.findCategory(page, limit, name);
        return RestBean.success(categories.getContent(), (int) categories.getTotalElements());
    }
}
