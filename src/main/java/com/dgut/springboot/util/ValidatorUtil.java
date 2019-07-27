package com.dgut.springboot.util;

import  java.util.regex.Matcher;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ValidatorUtil {
    private static final Pattern email_pattern =
            Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    public static boolean isEmail(String email){
        if(StringUtils.isEmpty(email)){
            return false;
        }
        else{
            Matcher m =email_pattern.matcher(email);
            return m.matches();
        }
    }
}
