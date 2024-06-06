package com.example.flowershop.service.impl;

import com.example.flowershop.dto.FlowerAndCategoryDto;
import com.example.flowershop.entity.*;
import com.example.flowershop.exception.GeneralException;
import com.example.flowershop.repositories.FlowerCategoryRepository;
import com.example.flowershop.repositories.FlowerRepository;
import com.example.flowershop.service.FlowerManageService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    JPAQueryFactory jpaQueryFactory;

    @Override
    public void addCategory(FlowerCategory flowerCategory) {
        //如果分类不存在则添加，否则返回“该分类已存在！”提示
        if (!flowerCategoryRepository.existsByName(flowerCategory.getName())) {
            flowerCategoryRepository.save(flowerCategory);
        } else {
            throw new GeneralException("该分类已存在！");
        }
    }

    @Override
    public void modifyCategory(FlowerCategory newFlowerCategory) {
        FlowerCategory category = flowerCategoryRepository.findById(newFlowerCategory.getId()).orElse(null);
        if (category != null) {
            flowerCategoryRepository.save(newFlowerCategory);
        } else {
            throw new GeneralException("无法修改：该分类不存在，请选择正确的分类！");
        }
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        flowerCategoryRepository.deleteById(categoryId);
    }

    @Override
    public Page<FlowerCategory> findCategory(Integer pageNo, Integer limit, String name) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit); //索引号=页码-1
        //使用QueryDSL动态查询
        QFlowerCategory qFlowerCategory = QFlowerCategory.flowerCategory;
        BooleanBuilder builder = new BooleanBuilder(); //用于构建动态查询
        if (name != null && !name.isEmpty()) {
            BooleanExpression expression = qFlowerCategory.name.containsIgnoreCase(name); //模糊查询
            builder.and(expression);
        }
        //单表查询所有字段的动态查询可借助jpa的findAll方法
        return flowerCategoryRepository.findAll(builder, pageable);
    }

    @Override
    public void addFlower(Flower flower) {
        //如果商品不存在则添加商品，否则提示“该商品已存在！”
        flower.setAddTime(new Date());
        if (!flowerRepository.existsByName(flower.getName())) {
            flowerRepository.save(flower);
        } else {
            throw new GeneralException("该商品已存在！");
        }
    }

    @Override
    public void modifyFlower(Flower newFlower) {
        newFlower.setAddTime(new Date()); //设置修改时间
        //修改之前查询商品是否存在
        Flower flower = flowerRepository.findById(newFlower.getId()).orElse(null);
        if (flower != null) {
            //如果上传图片（字节数组）为空，则还是使用之前的图片
            byte[] newPicture = newFlower.getPicture();
            if (newPicture == null || newPicture.length == 0) {
                newFlower.setPicture(flower.getPicture());
            }
            flowerRepository.save(newFlower);
        } else {
            throw new GeneralException("无法修改：该商品不存在，请选择正确的商品！");
        }
    }

    @Override
    public void deleteFlower(Integer flowerId) {
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        Long queryCount = jpaQueryFactory.select(qOrderDetail.count())
                .from(qOrderDetail)
                .where(qOrderDetail.flower.id.eq(flowerId))
                .fetchOne();
        if (queryCount != null && queryCount > 0L) {
            throw new GeneralException("无法删除：此商品已被购买");
        }
        flowerRepository.deleteById(flowerId);
    }

    @Override
    public Page<FlowerAndCategoryDto> findFlower(Integer pageNo, Integer limit, String name, String status) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit); //索引号=页码-1
        //使用QueryDSL动态查询
        QFlowerCategory qFlowerCategory = QFlowerCategory.flowerCategory;
        QFlower qFlower = QFlower.flower;
        //构建动态查询条件
        BooleanBuilder builder = new BooleanBuilder();
        if (name != null && !name.isEmpty()) {
            builder.and(qFlower.name.containsIgnoreCase(name)); //模糊匹配商品名
        }
        if (status != null && !status.isEmpty()) {
            if (status.equals("isOutOfStock")) { //如果传入isOutOfStock
                builder.and(qFlower.inventory.eq(0)); //则查询库存为0的商品
            } else {
                builder.and(qFlower.status.eq(status)); //正常匹配商品状态
            }
        }
        //基础查询
        List<FlowerAndCategoryDto> results = jpaQueryFactory
                .select(Projections.constructor(FlowerAndCategoryDto.class, //投影
                                qFlower.id, qFlower.name, qFlower.price, qFlower.description,
                                qFlower.picture, qFlower.inventory, qFlower.status, qFlower.addTime,
                                qFlowerCategory.name)
                        )
                .from(qFlower)
                .join(qFlowerCategory)
                .on(qFlower.flowerCategory.id.eq(qFlowerCategory.id))
                .where(builder) //动态查询条件
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        //查询总记录数
        Long queryCount = jpaQueryFactory.select(qFlower.count())
                .from(qFlower, qFlowerCategory)
                .where(qFlower.flowerCategory.id.eq(qFlowerCategory.id))
                .fetchOne();

        return PageableExecutionUtils.getPage(
                results,
                pageable,
                () -> queryCount != null ? queryCount : 0L //返回总行数，并处理空指针异常
        );
    }

}
