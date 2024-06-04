package com.example.flowershop.service;

import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
class FlowerManageServiceTest {
    @Autowired
    private FlowerManageService service;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void addCategory() {
        FlowerCategory category = FlowerCategory.builder()
                .name("分类3")
                .description("分类2的描述")
                .build();
        service.addCategory(category);
    }

    @Test
    void modifyCategory() {
        FlowerCategory category = FlowerCategory.builder()
                .name("分类1")
                .description("分类1的描述-修改")
                .build();
        service.modifyCategory(category);
    }

    @Test
    void deleteCategory() {
        service.deleteCategory(2);
    }

    @Test
    void findCategoryAllPage() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findCategory(1, 10, null)));
    }

    @Test
    void addFlower() {
        FlowerCategory category = FlowerCategory.builder()
                .id(1)
                .build();
        Flower flower = Flower.builder()
                .description("红玫瑰描述")
                .name("红玫瑰")
                .picture(null)
                .price(BigDecimal.valueOf(200.00))
                .status("已上架")
                .flowerCategory(category) //关联类别
                .build();
        service.addFlower(flower);
    }

    @Test
    void modifyFlower() {
        FlowerCategory category = FlowerCategory.builder()
                .id(3)
                .build();
        Flower flower = Flower.builder()
                .description("蓝玫瑰描述-修改")
                .name("蓝玫瑰")
                .picture(null)
                .price(BigDecimal.valueOf(200.00))
                .status("已上架")
                .flowerCategory(category) //关联类别
                .build();
        service.modifyFlower(flower);
    }

    @Test
    void deleteFlower() {
        service.deleteFlower(2);
    }

    @Test
    void findFlowerAllPage() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findFlower(1, 10, null, null)));
    }
}