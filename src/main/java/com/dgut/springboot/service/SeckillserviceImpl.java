package com.dgut.springboot.service;

import com.dgut.springboot.bean.Goods;
import com.dgut.springboot.bean.OrderInfo;
import com.dgut.springboot.bean.SeckillOrder;
import com.dgut.springboot.bean.User;
import com.dgut.springboot.redis.GoodsKey;
import com.dgut.springboot.redis.RedisService;
import com.dgut.springboot.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service("seckillService")
public class SeckillserviceImpl implements SeckillService {
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
    @Override
    @Transactional
    public OrderInfo killGoods(User user, GoodsVo goodsVo) {
        //        减库存，下订单，创建秒杀订单
        Goods goods = new Goods();
        boolean success = goodsService.reduceStock(goodsVo.getId());
        if(success){
            return orderService.createOrder(user,goodsVo);
        }else{
            setGoodsOver(goodsVo.getId());
            return null;
        }

    }

    private void setGoodsOver(Long id) {
        redisService.set(GoodsKey.getGoodsIsOver,""+id,true);
    }

    @Override
    public Long getSeckillResult(User user, Integer goodsId) {
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(seckillOrder==null){
            //没有秒杀到

//            如果秒杀完了
            boolean goodsOver = getGoodsOver(goodsId);
            if(goodsOver){
//                秒杀完了,秒杀失败
                return Long.valueOf(-1);
            }else{
//                排队中
                return Long.valueOf(0);
            }
        }
        return seckillOrder.getOrderId();
    }

    private boolean getGoodsOver(Integer goodsId) {
        String isOver =  redisService.get(GoodsKey.getGoodsIsOver,""+goodsId,String.class);
        if(isOver != null && "true".equals(isOver)){
            return true;
        }else{
            return false;

        }
    }
}
