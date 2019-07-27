package com.dgut;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class createUser {
    @Test
    public void createUserToken(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer = Charset.forName("UTF-8").encode("刘世杰");
        System.out.println(Charset.forName("UTF-8").decode(byteBuffer));
    }
}
