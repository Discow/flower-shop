package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.service.FlowerManageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin/")
public class FlowerManageController {
    @Resource
    FlowerManageService flowerManageService;

    @PostMapping("add-flower")
    public RestBean<?> addFlower(String categoryId, String name, String description,
                                 String price, String status,@RequestParam("image") MultipartFile image) {
        try {
            Flower flower = saveFlower(categoryId, name, description, price, status, image);

            if (flowerManageService.addFlower(flower)) {
                return RestBean.success("商品添加成功");
            } else {
                return RestBean.failure("商品添加失败");
            }
        } catch (IOException e) {
            return RestBean.failure("图片上传失败");
        }
    }

    @PostMapping("modify-flower")
    public RestBean<?> modifyFlower(String categoryId, String name, String description,
                                 String price, String status,@RequestParam("image") MultipartFile image) {
        try {
            Flower flower = saveFlower(categoryId, name, description, price, status, image);

            if (flowerManageService.modifyFlower(flower)) {
                return RestBean.success("商品修改成功");
            } else {
                return RestBean.failure("商品修改失败");
            }
        } catch (IOException e) {
            return RestBean.failure("图片上传失败");
        }
    }

    @GetMapping("delete-flower")
    public RestBean<?> deleteFlower(String flowerId) {
        if (flowerManageService.deleteFlower(Integer.valueOf(flowerId))) {
            return RestBean.success("商品删除成功");
        } else {
            return RestBean.failure("商品删除失败");
        }
    }

    @GetMapping("get-all-flower")
    public RestBean<?> getAllFlower(String page, String limit) {
        Page<Flower> flowers = flowerManageService.findFlowerAll(
                Integer.valueOf(page),
                Integer.valueOf(limit));
        return RestBean.success(flowers.getContent(), (int) flowers.getTotalElements());
    }

    @GetMapping("get-flower-byName")
    public RestBean<?> getFlowerByName(String name) {
        List<Flower> flowers = flowerManageService.findFlowerByNameLike(name);
        return RestBean.success(flowers, flowers.size());
    }

    private Flower saveFlower(String categoryId, String name, String description,
                              String price, String status, MultipartFile image) throws IOException {
        FlowerCategory category = FlowerCategory.builder()
                .id(Integer.valueOf(categoryId))
                .build();

        byte[] imageBytes = image.getBytes();

        return Flower.builder()
                .description(description)
                .name(name)
                .picture(imageBytes)
                .price(BigDecimal.valueOf(Long.parseLong(price)))
                .status(status)
                .flowerCategory(category) //关联类别
                .build();
    }
}
