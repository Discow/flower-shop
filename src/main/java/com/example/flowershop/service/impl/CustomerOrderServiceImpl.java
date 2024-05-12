package com.example.flowershop.service.impl;

import com.example.flowershop.dto.OrderItemDto;
import com.example.flowershop.entity.Order;
import com.example.flowershop.entity.OrderDetail;
import com.example.flowershop.entity.User;
import com.example.flowershop.repositories.FlowerRepository;
import com.example.flowershop.repositories.OrderDetailRepository;
import com.example.flowershop.repositories.OrderRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.repositories.projection.OrdersOnly;
import com.example.flowershop.service.CustomerOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {
    @Resource
    OrderRepository orderRepository;
    @Resource
    OrderDetailRepository orderDetailRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    FlowerRepository flowerRepository;

    private static final String PAID_STATUS = "已支付";
    private static final String CANCEL_STATUS = "已取消";
    private static final String DELETE_STATUS = "已删除";

    @Override
    public boolean addOrder(String email, String paymentType, String receiveType, String note, List<OrderItemDto> items) {
        //TODO 用户邮箱在controller层从spring security获取，想办法构建items列表
        User user = userRepository.findByEmail(email).orElse(null);

        try {
            Order order = new Order();
            order.setStatus(PAID_STATUS);
            order.setTotalAmount(calculateTotalAmount(items)); //由系统计算订单总金额
            order.setPaymentType(paymentType);
            order.setReceiveType(receiveType);
            order.setNote(note);
            order.setTime(new Date());
            order.setUser(user); //关联用户
            Integer orderId = orderRepository.saveAndFlush(order).getId(); //保存订单基本信息到订单表

            //设置关联商品信息（商品id，购买数量）
            ArrayList<OrderDetail> orderDetails = new ArrayList<>();
            for (OrderItemDto item : items) {
                OrderDetail.OrderDetailPK pk = new OrderDetail.OrderDetailPK();
                pk.setOrderId(orderId);
                pk.setFlowerId(item.getFlowerId());
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(pk);
                orderDetail.setQuantity(item.getQuantity());
                orderDetails.add(orderDetail);
            }
            orderDetailRepository.saveAll(orderDetails);

            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public boolean deleteOrder(Integer orderId) {
        //逻辑删除，将订单状态标记成已删除
        try {
            orderRepository.modifyOrderStatus(orderId, DELETE_STATUS);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public boolean cancelOrder(Integer orderId) {
        //将订单状态修改成已取消
        try {
            orderRepository.modifyOrderStatus(orderId, CANCEL_STATUS);
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Override
    public Page<OrdersOnly> findOrderAll(String email, Integer pageNo, Integer limit) {
        //TODO 在controller层从spring security获取当前用户
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        return userRepository.findOrderByEmail(email, pageable);
    }

    //计算订单总金额
    private BigDecimal calculateTotalAmount(List<OrderItemDto> items) {
        BigDecimal totalAmount = BigDecimal.ZERO; //初始化总金额为0
        for (OrderItemDto item : items) {
            BigDecimal price = flowerRepository.findPriceById(item.getFlowerId());
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(item.getQuantity()))); //单价*数量再累加
        }
        return totalAmount;
    }
}
