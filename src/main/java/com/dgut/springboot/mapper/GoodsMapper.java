package com.dgut.springboot.mapper;

import com.dgut.springboot.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface GoodsMapper {
    @Select("select * from goods g , seckill_goods sg WHERE sg.goods_id=g.id")
    public List<GoodsVo> getGoodsList();

    @Select("select * from goods g , seckill_goods sg WHERE sg.goods_id=g.id and g.id=#{id}")
    public GoodsVo getGoodsById(@Param("id") long id);
//库存减一,通过设置 stock_count>0 这个条件，让数据库来保证同步
    @Update("update seckill_goods SET stock_count = stock_count-1 where goods_id = #{goodsId} and stock_count>0")
    Integer reduceStock(@Param("goodsId") Long id);

    @Update("update seckill_goods set stock_count = 10 where id =#{id}")
    void setGoodsStrock(Long id);

    @Delete("delete from seckill_order")
    void delseckillOrder();

    @Delete("delete from order_info")
    void delOrderInfo();
}
