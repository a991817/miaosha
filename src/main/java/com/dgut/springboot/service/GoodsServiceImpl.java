package com.dgut.springboot.service;

import com.dgut.springboot.mapper.GoodsMapper;
import com.dgut.springboot.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("goodService")
public class GoodsServiceImpl implements GoodsService{
    @Autowired
    GoodsMapper goodsMapper;
    @Override
    public List<GoodsVo> getGoodsList() {
        return goodsMapper.getGoodsList();
    }

    @Override
    public GoodsVo getGoodsById(long id) {
        return goodsMapper.getGoodsById(id);
    }

    @Override
    public boolean reduceStock(Long id) {
        int colCount = goodsMapper.reduceStock(id);
        if(colCount>0){
            return true;
        }
        return false;
    }

    @Override
    public void setGoodsStrock(Long id) {
        goodsMapper.setGoodsStrock(id);
    }

    @Override
    public void delOrderTableInfo() {
        goodsMapper.delseckillOrder();
        goodsMapper.delOrderInfo();
    }
}
