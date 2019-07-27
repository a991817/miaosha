package com.dgut.springboot.rabbitmq;

import com.dgut.springboot.bean.OrderInfo;
import com.dgut.springboot.bean.User;
import com.dgut.springboot.redis.RedisService;
import com.dgut.springboot.result.Result;
import com.dgut.springboot.service.GoodsService;
import com.dgut.springboot.service.SeckillService;
import com.dgut.springboot.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    @Autowired
    SeckillService seckillService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
    private Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues="seckillQueue")
    public void receive(String message){
        logger.info("get msg "+message);
        SeckillOrderMsg seckillOrderMsg =  redisService.stringToBean(message,SeckillOrderMsg.class);
        User user = seckillOrderMsg.getUser();
        long goodsId = seckillOrderMsg.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
        //        减库存，下订单，创建秒杀订单
        seckillService.killGoods(user,goodsVo);
    }
}
