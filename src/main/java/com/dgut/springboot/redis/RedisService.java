package com.dgut.springboot.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


@Service
public class RedisService {
    @Autowired
    JedisPool jedisPool;

    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz)
    {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();/*调用jedisPool 对象的 getResource()方法 难道 jedis （其实就是一个连接）*/
            String realKey = keyPrefix.getPrefix() + ":"+ key ;
            System.out.println("从缓存中取的key:"+realKey);
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }
    public <T> boolean set(KeyPrefix keyPrefix, String key, T value)
    {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();/*调用jedisPool 对象的 getResource()方法 难道 jedis （其实就是一个连接）*/
            String realKey = keyPrefix.getPrefix() + ":"+ key ;
            String str = beanToString(value);
            jedis.set(realKey,str);
            if(keyPrefix.expireSeconds()>0) {
                //生存时间大于0才设置，等于0表示永久有效
                //设置key生存时间，当key过期时，它会被自动删除。
                jedis.expire(realKey, keyPrefix.expireSeconds());
            }
            System.out.println("保存到缓存中的key:"+realKey);
            return true;
        }finally {
            returnToPool(jedis);
        }
    }
//删除一个缓存
    public <T> boolean delete(KeyPrefix keyPrefix, String key)
    {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();/*调用jedisPool 对象的 getResource()方法 难道 jedis （其实就是一个连接）*/
            String realKey = keyPrefix.getPrefix() + ":"+ key ;
            jedis.del(realKey);
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    //减一
    public <T> long decr(KeyPrefix keyPrefix, String key)
    {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();/*调用jedisPool 对象的 getResource()方法 难道 jedis （其实就是一个连接）*/
            String realKey = keyPrefix.getPrefix() + ":"+ key ;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public static <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    public static <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        }else if(clazz == String.class) {
            return (T)str;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(str);
        }else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if( jedis != null ){
            jedis.close();
        }
    }


}
