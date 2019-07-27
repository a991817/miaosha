package com.dgut.springboot.redis;


public class
OrderKey extends BasePrefix {
//    1hour
    private static int getSeckillOrderSecond = 60*60;

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getSeckillOrderByUIDGID = new OrderKey(getSeckillOrderSecond,"uidgid");
}
