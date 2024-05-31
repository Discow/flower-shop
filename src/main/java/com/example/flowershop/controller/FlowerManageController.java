package com.example.flowershop.controller;

import com.example.flowershop.dto.FlowerAndCategoryDto;
import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.service.FlowerManageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

@RestController
@RequestMapping("/api/admin/")
public class FlowerManageController {
    @Resource
    FlowerManageService flowerManageService;

    @PostMapping("add-flower")
    public RestBean<?> addFlower(Integer categoryId, String name, String description,
                                 BigDecimal price, Integer inventory, String status, @RequestParam("image") String image) {
        try {
            Flower flower = saveFlower(null, categoryId, name, description, price, inventory, status, image);

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
    public RestBean<?> modifyFlower(Integer id, Integer categoryId, String name, String description,
                                    BigDecimal price, Integer inventory, String status,
                                    @RequestParam(value = "image", required = false) String image) {
        try {
            Flower flower = saveFlower(id, categoryId, name, description, price, inventory, status, image);

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
    public RestBean<?> deleteFlower(Integer flowerId) {
        if (flowerManageService.deleteFlower(flowerId)) {
            return RestBean.success("商品删除成功");
        } else {
            return RestBean.failure("商品删除失败");
        }
    }

    @GetMapping("get-flower")
    public RestBean<?> getFlower(Integer page, Integer limit,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) String status) {
        Page<FlowerAndCategoryDto> flowers = flowerManageService.findFlower(page, limit, name, status);
        return RestBean.success(flowers.getContent(), (int) flowers.getTotalElements());
    }

    private Flower saveFlower(Integer id, Integer categoryId, String name, String description,
                              BigDecimal price, Integer inventory, String status, String base64Image) throws IOException {
        FlowerCategory category = FlowerCategory.builder()
                .id(categoryId)
                .build();

        byte[] imageBytes = null;
        if (base64Image != null) {
            // 去除Base64编码前缀
            String base64Encoded = base64Image.substring(base64Image.indexOf(",") + 1);
            imageBytes = Base64.getDecoder().decode(base64Encoded);
        }

        return Flower.builder()
                .id(id)
                .description(description)
                .name(name)
                .picture(imageBytes)
                .price(price)
                .inventory(inventory)
                .status(status)
                .flowerCategory(category) //关联类别
                .build();
    }
}
