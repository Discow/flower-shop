package com.example.flowershop.service.impl;

import com.example.flowershop.dto.FlowerAndCategoryDto;
import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.FlowerCategory;
import com.example.flowershop.entity.QFlower;
import com.example.flowershop.entity.QFlowerCategory;
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
    @Resource
    JPAQueryFactory jpaQueryFactory;

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
        FlowerCategory category = flowerCategoryRepository.findById(newFlowerCategory.getId()).orElse(null);
        if (category != null) {
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
