package com.example.flowershop.service.impl;

import com.example.flowershop.dto.OrderDetailDto;
import com.example.flowershop.dto.UserInfoAndOrdersDto;
import com.example.flowershop.entity.*;
import com.example.flowershop.exception.GeneralException;
import com.example.flowershop.repositories.LogisticsRepository;
import com.example.flowershop.repositories.OrderRepository;
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

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service
public class OrderManageServiceImpl implements OrderManageService {
    @Resource
    OrderRepository orderRepository;
    @Resource
    LogisticsRepository logisticsRepository;
    @Resource
    JPAQueryFactory jpaQueryFactory;
    private static final String DELIVERY_STATUS = "已发货";

    @Override
    public void modifyOrderStatus(Integer orderId, String status) {
        orderRepository.modifyOrderStatus(orderId, status);
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
                        qOrder.id, qUser.email, qUser.username, qUser.phone,
                        new CaseBuilder()
                                .when(qOrder.status.in("paid_deleted", "unpaid_deleted"))
                                .then("由用户删除")
                                .otherwise(qOrder.status).as("status"),
                        qOrder.totalAmount, qOrder.paymentType, qOrder.receiveType, qOrder.note, qOrder.time
                ))
                .from(qOrder, qUser)
                .where(qOrder.user.id.eq(qUser.id), builder)
                .orderBy(qOrder.time.desc())
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
    public List<OrderDetailDto> findOrderDetail(Integer orderId) {
        //使用QueryDSL查询订单详情
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        QFlower qFlower = QFlower.flower;
        return jpaQueryFactory
                .select(Projections.fields(OrderDetailDto.class,
                        qFlower.id, qFlower.name, qFlower.price, qFlower.picture, qOrderDetail.quantity))
                .from(qOrderDetail, qFlower)
                .where(qOrderDetail.id.flowerId.eq(qFlower.id), qOrderDetail.id.orderId.eq(orderId))
                .fetch();
    }

    @Override
    public void doDelivery(Integer orderId, String company, String consignor) {
        QLogistics qLogistics = QLogistics.logistics;
        Logistics logistics = jpaQueryFactory
                .select(qLogistics)
                .from(qLogistics)
                .where(qLogistics.order.id.eq(orderId))
                .fetchOne();
        if (logistics != null) {
            logistics.setCompany(company);
            logistics.setConsignor(consignor);
            logisticsRepository.save(logistics);
            //更新订单状态为已发货
            orderRepository.modifyOrderStatus(orderId, DELIVERY_STATUS);
        } else {
            throw new GeneralException("该订单不存在！");
        }
    }

    @Override
    public Logistics findLogisticsByOrderId(Integer orderId) {
        Logistics logistics = logisticsRepository.findByOrderId(orderId);
        if (logistics == null) {
            throw new GeneralException("物流信息不存在！");
        }
        return logistics;
    }
}
