package com.dgut.springboot.vo;

import com.dgut.springboot.validator.isEmail;

import javax.validation.constraints.NotEmpty;

public class UserVo {

    @NotEmpty
    @isEmail
    private String email;
    @NotEmpty
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
