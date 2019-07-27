package com.dgut.springboot.redis;


public class GoodsKey extends BasePrefix{
    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public GoodsKey(String prefix) {
        super(prefix);
    }
    public static GoodsKey getGoodsListPage = new GoodsKey(60,"glPage");
    public static GoodsKey getGoodsDetailPage = new GoodsKey(60,"gdPage");
    public static GoodsKey getGoodsStock = new GoodsKey(0,"gs");

    public static GoodsKey getGoodsIsOver = new GoodsKey(0,"gOver");
}
