package com.dgut.springboot.service;

import com.dgut.springboot.bean.User;
import com.dgut.springboot.vo.UserVo;

import javax.servlet.http.HttpServletResponse;

public interface UserService {
    public User getUserById(String id);
    public String login(HttpServletResponse httpServletResponse, UserVo userVo);
    public User getUserByToken(String token,HttpServletResponse httpServletResponse);
    public void addCookie(String token,User user,HttpServletResponse httpServletResponse);
}
