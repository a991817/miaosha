package com.dgut.springboot.service;

import com.dgut.springboot.bean.User;
import com.dgut.springboot.exception.GlobalException;
import com.dgut.springboot.mapper.UserMapper;
import com.dgut.springboot.redis.KeyPrefix;
import com.dgut.springboot.redis.RedisService;
import com.dgut.springboot.redis.UserKey;
import com.dgut.springboot.result.CodeMsg;
import com.dgut.springboot.util.MD5Util;
import com.dgut.springboot.util.UUIDUtil;
import com.dgut.springboot.vo.UserVo;
import jdk.nashorn.internal.parser.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service("userService")
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisService redisService;
    public static final String COOKIE_NAME = "token";

    public User getUserById(String id){
//        先从缓存中取
        User user = redisService.get(UserKey.getById,id,User.class);
//        如果缓存中取不到，则从数据库中取，并保存在缓存中
        if(user == null){
            user = userMapper.getUserById(id);
            redisService.set(UserKey.getById,id,user);
        }
        return user;
    }

    public boolean changePassword(String token, String userId,String newPassword){
        User user = getUserById(userId);
        if(user==null){
//            如果取不到这个用户，则抛出异常
            throw new GlobalException(CodeMsg.EMAIL_NOT_EXIT);
        }else {
//            更新数据库中的信息
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setPassword(MD5Util.formPassToDBPass(newPassword,user.getSalt()));
            userMapper.updateUser(updateUser);
//            更改缓存中的信息
            redisService.delete(UserKey.getById,userId);
            user.setPassword(updateUser.getPassword());
            redisService.set(UserKey.token,token,user);

        }
        return true;
    }

    public String login(HttpServletResponse httpServletResponse, UserVo userVo) {
        String email = userVo.getEmail();
//        从数据库中取数据
        User user = getUserById(email);
        if(user == null){
            throw new GlobalException(CodeMsg.EMAIL_NOT_EXIT);
        }
        //判断密码是否正确
        String passDb = user.getPassword();
        String saltDb = user.getSalt();
        String calPass = MD5Util.formPassToDBPass(userVo.getPassword(),saltDb);
        //判断是否正确
        if(!passDb.equals(calPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
//        登陆成功后保存到cookie中
        String token = UUIDUtil.getUUID();
        addCookie(token,user,httpServletResponse);
        return token;
    }
//通过token获取User
    public User getUserByToken(String token,HttpServletResponse httpServletResponse) {
        if(StringUtils.isEmpty(token)){
            return null;
        }
        User user = redisService.get(UserKey.token,token,User.class);
//        延长有效期
        addCookie(token,user,httpServletResponse);
        return user;
    }

    public void addCookie(String token,User user,HttpServletResponse httpServletResponse){
//        保存在redis中
        redisService.set(UserKey.token,token,user);
        Cookie cookie = new Cookie(COOKIE_NAME,token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }
}
