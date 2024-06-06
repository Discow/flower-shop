package com.example.flowershop.service.impl;

import com.example.flowershop.dto.CartDto;
import com.example.flowershop.dto.OrderItemDto;
import com.example.flowershop.entity.Flower;
import com.example.flowershop.repositories.FlowerRepository;
import com.example.flowershop.service.CartService;
import com.example.flowershop.service.CustomerOrderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 购物车业务逻辑
 */
@Transactional
@Service
public class CartServiceImpl implements CartService {
    @Resource
    private FlowerRepository flowerRepository;
    @Resource
    private CustomerOrderService customerOrderService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    //购物车key的前缀
    private static final String CART_KEY_PREFIX = "cart:user:";

    @Override
    public void addCart(String email, OrderItemDto orderItemDto) {
        String key = CART_KEY_PREFIX + email;
        //如何不存在则添加
        Boolean hasKey = redisTemplate.opsForHash().getOperations().hasKey(key);
        if (Boolean.FALSE.equals(hasKey)) {
            redisTemplate.opsForHash().put(key, orderItemDto.getFlowerId(), orderItemDto.getQuantity());
        } else {
            redisTemplate.opsForHash().put(key, orderItemDto.getFlowerId(), orderItemDto.getQuantity());
            redisTemplate.expire(key, 90, TimeUnit.DAYS); //设置过期时间
        }
    }

    @Override
    public void delCart(String email, Integer flowerId) {
        String key = CART_KEY_PREFIX + email;
        redisTemplate.opsForHash().delete(key, flowerId);
    }

    @Override
    public void updateCart(String email, OrderItemDto orderItemDto) {
        String key = CART_KEY_PREFIX + email;
        redisTemplate.opsForHash().put(key, orderItemDto.getFlowerId(), orderItemDto.getQuantity());
    }

    @Override
    public List<CartDto> findAll(String email) {
        String key = CART_KEY_PREFIX + email;
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        ArrayList<CartDto> cartList = new ArrayList<>();
        map.forEach((k,v)->{
            //根据商品id获取完整的商品信息
            Flower flower = flowerRepository.findById((Integer) k).orElse(null);
            if (flower != null) {
                CartDto cartDto = CartDto.builder()
                        .flowerId(flower.getId())
                        .flowerName(flower.getName())
                        .price(flower.getPrice())
                        .inventory(flower.getInventory())
                        .picture(flower.getPicture())
                        .quantity((Integer) v)
                        .build();
                cartList.add(cartDto);
            }
        });
        return cartList;
    }

    @Override
    public void buy(String email, String paymentType, String receiveType, String note) {
        String key = CART_KEY_PREFIX + email;
        //提交订单
        customerOrderService.addOrder(email, paymentType, receiveType, note, getCartBaseInfo(key));
        //清空购物车
        redisTemplate.delete(key);
    }

    @Override
    public BigDecimal getTotalAmount(String email) {
        String key = CART_KEY_PREFIX + email;
        return calculateTotalAmount(getCartBaseInfo(key));
    }

    //从redis获取购物车商品和数量信息
    private List<OrderItemDto> getCartBaseInfo(String key) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        List<OrderItemDto> items = new ArrayList<>();
        map.forEach((k,v)->{
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setFlowerId((Integer) k);
            orderItemDto.setQuantity((Integer) v);
            items.add(orderItemDto);
        });
        return items;
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
