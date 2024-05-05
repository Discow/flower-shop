package com.example.flowershop;

import com.example.flowershop.dto.OrderItemDto;
import com.example.flowershop.entity.Flower;
import com.example.flowershop.entity.Order;
import com.example.flowershop.entity.OrderDetail;
import com.example.flowershop.entity.User;
import com.example.flowershop.repositories.FlowerRepository;
import com.example.flowershop.repositories.OrderDetailRepository;
import com.example.flowershop.repositories.OrderRepository;
import com.example.flowershop.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

@Transactional
@Commit
public class JpaTest extends FlowerShopApplicationTests {
    @Resource
    UserRepository userRepository;

    @Resource
    FlowerRepository flowerRepository;

    @Resource
    OrderRepository orderRepository;

    @Resource
    OrderDetailRepository orderDetailRepository;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //用户注册
    @Test
    void test1() {
        User user = User.builder()
                .email("admin@qq.com")
                .username("测试管理员")
                .password(encoder.encode("000000"))
                .phone("13733332222")
                .role(User.Role.ADMIN)
                .build();

        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            System.out.println("用户添加成功");
        } else {
            // 处理用户已存在的情况
            System.out.println("该用户已存在！");
        }
    }

    //修改密码
    @Test
    void test2() {
        User newUser = User.builder()
                .email("123456@qq.com")
                .username("小明")
                .password("123456")
                .phone("13733332222")
                .role(User.Role.USER)
                .build();

        //根据邮箱查询主键
        User user = userRepository.findByEmail(newUser.getEmail()).orElse(null); //查不到返回null而不是抛出异常
        if (user != null) {
            newUser.setId(user.getId());
        }

        userRepository.save(newUser);
    }

    //商品上架
    @Test
    void test3() {
        Flower flower = Flower.builder()
                .addTime(new Date())
                .description("蓝玫瑰描述")
                .name("蓝玫瑰")
                .picture(null)
                .price(BigDecimal.valueOf(200.00))
                .status("已上架")
                .build();

        if (!flowerRepository.existsByName(flower.getName())) {
            flowerRepository.save(flower);
            System.out.println("商品添加成功");
        } else {
            System.out.println("该商品已存在！");
        }
    }

    //商品修改
    @Test
    void test4() {
        Flower newFlower = Flower.builder()
                .addTime(new Date())
                .description("红玫瑰描述")
                .name("红玫瑰")
                .picture(null)
                .price(BigDecimal.valueOf(5.00))
                .status("已上架")
                .build();

        Flower flower = flowerRepository.findByName(newFlower.getName()).orElse(null);
        if (flower != null) {
            newFlower.setId(flower.getId());
        }
        flowerRepository.save(newFlower);
    }

    //用户下单
    @Test
    void test5() {
        User user = userRepository.findByEmail("123456@qq.com").orElse(null);

        //设置订单信息
        Order order = Order.builder()
                .paymentType("支付宝")
                .receiveType("同城快递")
                .status("待配送")
                .time(new Date())
                .totalAmount(BigDecimal.valueOf(300.00))
                .user(user) //关联下单用户
                .build();

        //模拟一个订单有多个商品
        ArrayList<OrderItemDto> items = new ArrayList<>();
        items.add(new OrderItemDto(1, 1));
        items.add(new OrderItemDto(2, 1));

        Integer orderId = orderRepository.saveAndFlush(order).getId(); //保存订单基本信息到订单表


        //设置关联信息
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
    }

    //根据订单号查看订单
    @SneakyThrows
    @Test
    void test6() {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(orderRepository.findById(9).orElse(null));
        System.out.println(s);
    }

    //根据用户邮箱查看订单
    @SneakyThrows
    @Test
    void test7() {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(userRepository.findByEmail("123456@qq.com").orElse(null));
        System.out.println(s);
    }

    //根据订单号删除订单
    @Test
    void test8() {
        orderRepository.deleteById(9);
    }
}
