package com.example.flowershop.service;

import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.math.BigDecimal;

@SpringBootTest
@Commit
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
        System.out.println(service.addCategory(category));
    }

    @Test
    void modifyCategory() {
        FlowerCategory category = FlowerCategory.builder()
                .name("分类1")
                .description("分类1的描述-修改")
                .build();
        System.out.println(service.modifyCategory(category));
    }

    @Test
    void deleteCategory() {
        System.out.println(service.deleteCategory(2));
    }

    @Test
    void findCategoryAll() throws JsonProcessingException {
        String s = objectMapper.writeValueAsString(service.findCategoryAll());
        System.out.println(s);
    }

    @Test
    void findCategoryAllPage() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findCategoryAll(1, 10)));
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
        System.out.println(service.addFlower(flower));
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
        System.out.println(service.modifyFlower(flower));
    }

    @Test
    void deleteFlower() {
        System.out.println(service.deleteFlower(2));
    }

    @Test
    void findFlowerAll() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findFlowerAll()));
    }

    @Test
    void findFlowerAllPage() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findFlowerAll(1,10)));
    }

    @Test
    void findFlowerByNameLike() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findFlowerByNameLike("玫瑰")));
    }

    @Test
    void findByCategoryName() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(service.findByCategoryName("分类1",1,10)));
    }
}