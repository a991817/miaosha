package com.dgut.springboot.controller;

import com.dgut.springboot.mapper.UserMapper;
import com.dgut.springboot.result.CodeMsg;
import com.dgut.springboot.result.Result;
import com.dgut.springboot.service.UserService;
import com.dgut.springboot.util.UserUtil;
import com.dgut.springboot.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> do_login(@Valid UserVo userVo, HttpServletResponse httpServletResponse){

        logger.info(userVo.toString());
        String token  = userService.login(httpServletResponse,userVo);
        return Result.success(token);
    }




}
