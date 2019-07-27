package com.dgut.springboot.service;

import com.dgut.springboot.bean.OrderInfo;
import com.dgut.springboot.bean.User;
import com.dgut.springboot.vo.GoodsVo;
import org.springframework.stereotype.Service;

public interface SeckillService {
    OrderInfo killGoods(User user, GoodsVo goodsVo);

    Long getSeckillResult(User user, Integer goodsId);
}
