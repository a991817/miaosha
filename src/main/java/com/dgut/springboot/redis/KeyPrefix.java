package com.dgut.springboot.redis;

public interface KeyPrefix {
//    过期时间     0表示永不过期
    public int expireSeconds();
//    获得前缀

    public String getPrefix();
}
