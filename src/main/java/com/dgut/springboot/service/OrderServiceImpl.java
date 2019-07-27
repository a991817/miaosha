package com.dgut.springboot.service;

import com.dgut.springboot.bean.OrderInfo;
import com.dgut.springboot.bean.SeckillOrder;
import com.dgut.springboot.bean.User;
import com.dgut.springboot.mapper.OrderMapper;
import com.dgut.springboot.redis.OrderKey;
import com.dgut.springboot.redis.RedisService;
import com.dgut.springboot.vo.GoodsVo;
import com.dgut.springboot.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    RedisService redisService;
    @Override
    public SeckillOrder getSeckillOrderByUserIdGoodsId(String userId, Integer goodsId) {
        SeckillOrder seckillOrder = redisService.get(OrderKey.getSeckillOrderByUIDGID,userId+"-"+goodsId,SeckillOrder.class);
        if(seckillOrder!=null){
            return seckillOrder;
        }
        return orderMapper.getSeckillOrderByUserIdGoodsId(userId,goodsId);
    }

    @Transactional
    @Override
    public OrderInfo createOrder(User user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insertOrder(orderInfo);


        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);
//放入缓存中
        redisService.set(OrderKey.getSeckillOrderByUIDGID,seckillOrder.getUserId()+"-"+seckillOrder.getGoodsId(),seckillOrder);
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderByUserIdGoodsId(String userid, Long goodsId) {
        return orderMapper.getOrderByUserIdGoodsId(userid,goodsId);
    }

    @Override
    public OrderDetailVo getOrderDetailByOrderId(Long orderId) {
        return orderMapper.getOrderDetailByOrderId(orderId);
    }
}
