package com.dgut.springboot.redis;

public class UserKey extends BasePrefix{

    private static final int TOKEN_EXPIRE_SECOND = 3600*24*2;

    private UserKey(String prefix) {
        super(prefix);
    }

    private UserKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    public static UserKey getById = new UserKey("id");

    public static UserKey getByName = new UserKey("name");

//形如："UserKey:token:6f8e3b3e55684ea3b68a1969e61d4a8d"
    public static UserKey token = new UserKey(TOKEN_EXPIRE_SECOND,"token");
}
