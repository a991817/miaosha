package com.dgut.springboot.util;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String MD5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass){
        String src = ""+salt.charAt(2)+salt.charAt(3)+inputPass+salt.charAt(4)+salt.charAt(0);
        System.out.println(src);
        return MD5(src);
    }

    public static String formPassToDBPass(String inputPass,String salt){
        String src = ""+salt.charAt(2)+salt.charAt(3)+inputPass+salt.charAt(4)+salt.charAt(0);
        return MD5(src);
    }

    public static String inputPassToDBPass(String inputPass,String saltDB){
        String formPass = inputPassToFormPass(inputPass);
        String DBPass = formPassToDBPass(formPass,saltDB);
        return DBPass;
    }
}
