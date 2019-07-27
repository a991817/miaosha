package com.dgut.springboot.service;

import com.dgut.springboot.bean.OrderInfo;
import com.dgut.springboot.bean.SeckillOrder;
import com.dgut.springboot.bean.User;
import com.dgut.springboot.mapper.OrderMapper;
import com.dgut.springboot.vo.GoodsVo;
import com.dgut.springboot.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

public interface OrderService {
    public SeckillOrder getSeckillOrderByUserIdGoodsId(String id, Integer goodsId);

    OrderInfo createOrder(User user, GoodsVo goodsVo);

    public OrderInfo getOrderByUserIdGoodsId(String id, Long goodsId);

    OrderDetailVo getOrderDetailByOrderId(Long orderId);
}
