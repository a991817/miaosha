package com.dgut.springboot.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static String SECKILL_QUEUE = "seckillQueue";
    @Bean
    public Queue queue(){
        return new Queue(SECKILL_QUEUE,true);
    }
}
