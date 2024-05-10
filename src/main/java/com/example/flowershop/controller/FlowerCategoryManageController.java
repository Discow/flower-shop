package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.service.FlowerManageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if (flowerManageService.addCategory(category)) {
            return RestBean.success("分类添加成功");
        } else {
            return RestBean.failure("分类添加失败");
        }
    }

    @PostMapping("modify-category")
    public RestBean<?> modifyCategory(String name, String description) {
        FlowerCategory category = new FlowerCategory();
        category.setName(name);
        category.setDescription(description);
        if (flowerManageService.modifyCategory(category)) {
            return RestBean.success("分类修改成功");
        } else {
            return RestBean.failure("分类修改失败");
        }
    }

    @GetMapping("delete-category")
    public RestBean<?> deleteCategory(String categoryId) {
        if (flowerManageService.deleteCategory(Integer.valueOf(categoryId))) {
            return RestBean.success("分类删除成功");
        } else {
            return RestBean.failure("分类删除失败");
        }
    }

    @GetMapping("get-all-category")
    public RestBean<?> getAllCategory(String page, String limit) {
        Page<FlowerCategory> categories = flowerManageService.findCategoryAll(
                Integer.valueOf(page),
                Integer.valueOf(limit));
        return RestBean.success(categories.getContent(), (int) categories.getTotalElements());
    }
}
