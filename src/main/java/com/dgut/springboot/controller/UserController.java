package com.dgut.springboot.controller;

import com.dgut.springboot.bean.User;
import com.dgut.springboot.mapper.UserMapper;
import com.dgut.springboot.redis.RedisService;
import com.dgut.springboot.redis.UserKey;
import com.dgut.springboot.result.CodeMsg;
import com.dgut.springboot.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
//    Controller返回有两种，一种是rest，一种是跳转页面
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisService redisService;
    @GetMapping("/user")
    public User insertUser(User user){
//        userMapper.insertUser(user);
        return user;
    }

    @GetMapping("/user/{id}")
    public User selectOne(@PathVariable("id") String id){
//        return userMapper.selectOne(id);
        return null;
    }

    @RequestMapping("/error123")
    @ResponseBody
    public Result<String> error(){
        return Result.error(CodeMsg.SERVER_ERROR);
    }
    @RequestMapping("/success123")
    @ResponseBody
    public Result<String> success(){
        return Result.success("成功了");
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user =redisService.get(UserKey.getById,"1",User.class);
        return Result.success(user);
    }
    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
//        User user = new User();
//        user.setUsername("张三");
//        user.setId("1");
//        Boolean flag =redisService.set(UserKey.getById,"1",user);
//        return Result.success(flag);
        return null;
    }

}
