package com.dgut.springboot.rabbitmq;

import com.dgut.springboot.redis.RedisService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    @Autowired
    RedisService redisService;
    @Autowired
    AmqpTemplate amqpTemplate;

//    public void send(Object message){
//        String msg = redisService.beanToString(message);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE_NAME,msg);
//    }

    public void sendSeckillOrderMsg(SeckillOrderMsg message) {
        String msg = redisService.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE,msg);
    }
}
