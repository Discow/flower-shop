package com.example.flowershop.service.impl;

import com.example.flowershop.dto.OrderDetailDto;
import com.example.flowershop.dto.OrderItemDto;
import com.example.flowershop.entity.*;
import com.example.flowershop.exception.GeneralException;
import com.example.flowershop.repositories.*;
import com.example.flowershop.service.CustomerOrderService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @Resource
    LogisticsRepository logisticsRepository;
    @Resource
    JPAQueryFactory jpaQueryFactory;

    private static final String PAID_STATUS = "已支付";
    private static final String CANCEL_STATUS = "已取消";
    private static final String RECEIPTED_STATUS = "已完成";
    private static final String PAID_DELETED_STATUS = "paid_deleted";
    private static final String UNPAID_DELETED_STATUS = "unpaid_deleted";

    @Override
    public void addOrder(String email, String paymentType, String receiveType, String note, List<OrderItemDto> items,
                         String receiver, String address, Date deliveryTime) {
        //TODO 用户邮箱在controller层从spring security获取，想办法构建items列表
        User user = userRepository.findByEmail(email).orElse(null);

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
            //查询商品的库存是否充足
            Flower flower = flowerRepository.findById(item.getFlowerId()).orElse(null);
            if (flower == null) {
                throw new GeneralException("商品不存在！");
            }
            Integer inventory = flower.getInventory();
            //减少对应商品的库存
            int newInventory = inventory - item.getQuantity();
            //当前库存为0或购买数量超过库存都返回库存不足
            if (inventory == 0 || newInventory < 0) {
                throw new GeneralException("库存不足！");
            }
            flowerRepository.updateInventory(flower.getId(), newInventory);
            //设置关联信息
            OrderDetail.OrderDetailPK pk = new OrderDetail.OrderDetailPK();
            pk.setOrderId(orderId);
            pk.setFlowerId(item.getFlowerId());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setId(pk);
            orderDetail.setQuantity(item.getQuantity());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);

        //如果选择配送则添加配送信息
        if (receiver != null) {
            Logistics logistics = new Logistics();
            logistics.setReceiver(receiver);
            logistics.setAddress(address);
            logistics.setEstimated_time(deliveryTime);
            logistics.setOrder(Order.builder().id(orderId).build()); //关联订单
            logisticsRepository.save(logistics);
        }
    }

    @Override
    public void deleteOrder(String email, Integer orderId) {
        //逻辑删除，将订单状态标记成已删除，如果订单已完成，则标记为‘paid_deleted’，如果订单在取消后删除，则标记为unpaid_deleted
        // TODO 安全漏洞警告：应当限制只能删除当前登录用户所拥有的订单
        if (!checkOrderPermission(email, orderId)) { //没有权限则失败
            throw new GeneralException("No Permission!");
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && RECEIPTED_STATUS.equals(order.getStatus())) { //订单已完成的删除操作
            orderRepository.modifyOrderStatus(orderId, PAID_DELETED_STATUS);
        } else {
            orderRepository.modifyOrderStatus(orderId, UNPAID_DELETED_STATUS); //订单已取消的删除操作
        }
    }

    @Override
    public void cancelOrder(String email, Integer orderId) {
        //将订单状态修改成已取消
        // TODO 安全漏洞警告：应当限制只能取消当前登录用户所拥有的订单
        if (!checkOrderPermission(email, orderId)) { //没有权限则失败
            throw new GeneralException("No Permission!");
        }
        orderRepository.modifyOrderStatus(orderId, CANCEL_STATUS);
    }

    @Override
    public void confirmReceipt(String email, Integer orderId) {
        //将订单状态修改成已完成
        // TODO 安全漏洞警告：应当限制只能取消当前登录用户所拥有的订单
        if (!checkOrderPermission(email, orderId)) { //没有权限则失败
            throw new GeneralException("No Permission!");
        }
        //如果订单已取消或者已删除则不能再确认收货
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getStatus().equals(CANCEL_STATUS)
                || order.getStatus().equals(PAID_DELETED_STATUS)
                || order.getStatus().equals(UNPAID_DELETED_STATUS)) {
            throw new GeneralException("错误：无法确认收货（当前订单已取消）");
        }
        orderRepository.modifyOrderStatus(orderId, RECEIPTED_STATUS);
    }

    @Override
    public Page<Order> findOrder(String email, String status, Integer pageNo, Integer limit) {
        //TODO 在controller层从spring security获取当前用户
        Pageable pageable = PageRequest.of(pageNo - 1, limit);
        //获取用户id
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new GeneralException("用户不存在！");
        }
        //使用QueryDSL动态查询
        QOrder qOrder = QOrder.order;
        //构建动态查询语句
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qOrder.user.id.eq(user.getId()));
        if (status != null && !status.isEmpty()) {
            builder.and(qOrder.status.eq(status));
        } else {
            builder.and(qOrder.status.notIn(PAID_DELETED_STATUS, UNPAID_DELETED_STATUS));
        }
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "time")); //重新按照时间降序排序
        return orderRepository.findAll(builder, pageableWithSort);
    }

    @Override
    public List<OrderDetailDto> findOrderDetail(String email, Integer orderId) {
        //获取用户id，防止顾客查询其他用户的订单
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new GeneralException("找不到用户！");
        }
        //使用QueryDsl查询
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        QFlower qFlower = QFlower.flower;
        QOrder qOrder = QOrder.order;
        QComment qComment = QComment.comment;
        //构建查询语句
        return jpaQueryFactory.select(Projections.constructor(OrderDetailDto.class,
                        qFlower.id, qFlower.name, qFlower.price, qFlower.picture, qOrderDetail.quantity,
                        qComment.rating, qComment.content
                ))
                .from(qOrderDetail, qFlower, qOrder)
                .leftJoin(qComment)
                .on(qComment.id.flowerId.eq(qFlower.id), qComment.id.userId.eq(qOrder.user.id))
                .where(qOrderDetail.id.flowerId.eq(qFlower.id), qOrderDetail.id.orderId.eq(qOrder.id), //连接四个表
                        qOrder.user.id.eq(user.getId()), qOrderDetail.id.orderId.eq(orderId)) //通过订单号查询指定用户的订单
                .fetch();
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

    //订单权限校验，检查订单是否属于当前用户
    boolean checkOrderPermission(String email, Integer orderId) {
        //获取当前登录的用户id
        Integer currentUserId = userRepository.findByEmail(email).map(User::getId).orElse(null);
        //获取订单所属的用户id
        Integer orderUserId = orderRepository.findById(orderId).map(Order::getUser).map(User::getId).orElse(null);
        //如果取消的不是属于自己的订单则失败
        return Objects.equals(currentUserId, orderUserId);
    }
}
