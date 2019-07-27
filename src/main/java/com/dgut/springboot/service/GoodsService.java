package com.dgut.springboot.service;

import com.dgut.springboot.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    public List<GoodsVo> getGoodsList();

    public GoodsVo getGoodsById(long id);

    boolean reduceStock(Long id);

    void setGoodsStrock(Long id);

    void delOrderTableInfo();
}
