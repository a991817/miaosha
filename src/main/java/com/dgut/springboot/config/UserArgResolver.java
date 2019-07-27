package com.dgut.springboot.config;

import com.dgut.springboot.bean.User;
import com.dgut.springboot.service.UserService;
import com.dgut.springboot.service.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Service
public class UserArgResolver implements HandlerMethodArgumentResolver {
    @Autowired
    UserService userService;
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz==User.class;
    }
//上面返回true表示支持这个参数，然后做下面的处理
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = nativeWebRequest.getNativeResponse(HttpServletResponse.class);

        String paramToken = httpServletRequest.getParameter(UserServiceImpl.COOKIE_NAME);
        String cookieToken = getCookieToken(httpServletRequest,UserServiceImpl.COOKIE_NAME);

        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)){
            return null;
        }
//        获取token
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
//        取出user
        return userService.getUserByToken(token,httpServletResponse);

    }
    private String getCookieToken(HttpServletRequest httpServletRequest, String cookieName) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies==null ||cookies.length<=0){
            return null;
        }
        for (Cookie cookie:cookies) {
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return null;
    }
}
