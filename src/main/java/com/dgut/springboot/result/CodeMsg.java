package com.dgut.springboot.result;

public class CodeMsg {
    private int code;
    private String msg;
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101,"参数校验异常:%s");

    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500201,"密码不能为空");
    public static CodeMsg EMAIL_EMPTY = new CodeMsg(500202,"邮箱不能为空");
    public static CodeMsg EMAIL_ERROR = new CodeMsg(500203,"邮箱格式错误123");
    public static CodeMsg EMAIL_NOT_EXIT = new CodeMsg(500204,"邮箱不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500205,"密码错误");
//    秒杀模块 5003XX
    public static CodeMsg NO_STOCK = new CodeMsg(500301,"库存已经空了");
    public static CodeMsg NO_REPEAT_KILL = new CodeMsg(500302,"已经秒杀过了");

    private CodeMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object...args){
        int code = this.code;
        String message = String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
