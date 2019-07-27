package com.dgut.springboot.controller;

import com.dgut.springboot.rabbitmq.MQReceiver;
import com.dgut.springboot.rabbitmq.MQSender;
import com.dgut.springboot.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @Autowired
    MQSender sender;
    @Autowired
    MQReceiver receiver;
    @RequestMapping("/testRabbitMQ")
    @ResponseBody
    public Result<String> testRabbitMQ(){
//        sender.send("hello rabbitmq!");
        return Result.success("success");
    }

}
