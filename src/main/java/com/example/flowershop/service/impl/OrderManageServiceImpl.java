package com.example.flowershop.service.impl;

import com.example.flowershop.dto.UserInfoAndOrdersDto;
import com.example.flowershop.entity.QOrder;
import com.example.flowershop.entity.QUser;
import com.example.flowershop.repositories.OrderRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.repositories.projection.UserInfoAndOrders;
import com.example.flowershop.service.OrderManageService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class OrderManageServiceImpl implements OrderManageService {
    @Resource
    OrderRepository orderRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean modifyOrderStatus(Integer orderId, String status) {
        try {
            orderRepository.modifyOrderStatus(orderId, status);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public Page<UserInfoAndOrdersDto> findOrder(Integer pageNo, Integer limit, String receiveType, String status, String phone) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        //使用QueryDSL动态查询
        QOrder qOrder = QOrder.order;
        QUser qUser = QUser.user;
        //构建动态查询条件
        BooleanBuilder builder = new BooleanBuilder();
        if (receiveType != null && !receiveType.isEmpty()) {
            builder.and(qOrder.receiveType.eq(receiveType)); //匹配收货方式
        }
        if (status != null && !status.isEmpty()) {
            builder.and(qOrder.status.eq(status)); //匹配订单状态
        }
        if (phone != null && !phone.isEmpty()) {
            builder.and(qUser.phone.containsIgnoreCase(phone)); //模糊匹配手机号
        }
        //基础查询
        List<UserInfoAndOrdersDto> results = jpaQueryFactory
                .select(Projections.constructor(UserInfoAndOrdersDto.class,
                        qUser.id, qUser.email, qUser.username, qUser.phone,
                        new CaseBuilder()
                                .when(qOrder.status.in("paid_deleted", "unpaid_deleted"))
                                .then("由用户删除")
                                .otherwise(qOrder.status).as("status"),
                        qOrder.totalAmount, qOrder.paymentType, qOrder.receiveType, qOrder.note, qOrder.time
                ))
                .from(qOrder, qUser)
                .where(qOrder.user.id.eq(qUser.id), builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        //查询总行数
        Long queryCount = jpaQueryFactory.select(qOrder.count())
                .from(qOrder, qUser)
                .where(qOrder.user.id.eq(qUser.id))
                .fetchOne();
        return PageableExecutionUtils.getPage(results, pageable, () -> queryCount != null ? queryCount : 0L);
    }

    @Override
    public Page<UserInfoAndOrders> findOrderById(Integer id, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return orderRepository.findOrderById(id, pageable);
    }

    @Override
    public Page<UserInfoAndOrders> findOrderByUserName(String username, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return userRepository.findOrderByUsername(username, pageable);
    }

    @Override
    public Page<UserInfoAndOrders> findOrderByPhone(String phone, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return userRepository.findOrderByPhone(phone, pageable);
    }

    //TODO
    @Override
    public Page<UserInfoAndOrders> findOrderByCategory(Integer category, Integer pageNo, Integer limit) {
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return null;
    }

}
