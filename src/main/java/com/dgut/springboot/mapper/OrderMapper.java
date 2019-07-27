package com.dgut.springboot.mapper;

import com.dgut.springboot.bean.OrderInfo;
import com.dgut.springboot.bean.SeckillOrder;
import com.dgut.springboot.vo.OrderDetailVo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OrderMapper {
    @Select("select * from seckill_order where user_id=#{userId} and goods_id=#{goodsId}")
    SeckillOrder getSeckillOrderByUserIdGoodsId(@Param("userId")String userId, @Param("goodsId")Integer goodsId);

    @Insert("insert into order_info(user_id,goods_id,delivery_addr_id,goods_name,goods_price,goods_count,order_channel,status,create_date,pay_date)\n" +
            "values(#{userId},#{goodsId},#{deliveryAddrId},#{goodsName},#{goodsPrice},#{goodsCount},#{orderChannel},#{status}," +
            "date_format(#{createDate},'%Y-%m-%d %H:%i:%S'),date_format(#{payDate},'%Y-%m-%d %H:%i:%S'))")
    @SelectKey(keyColumn = "id",keyProperty = "id",resultType = Long.class,before = false,statement = "select last_insert_id()")
    public Long insertOrder(OrderInfo orderInfo);

    @Insert("insert into seckill_order(user_id,order_id,goods_id)" +
            "values(#{userId},#{orderId},#{goodsId})")
    public void insertSeckillOrder(SeckillOrder seckillOrder);

    @Select("select * from order_info where user_id=#{userId} and goods_id=#{goodsId}")
    OrderInfo getOrderByUserIdGoodsId(@Param("userId")String userId, @Param("goodsId")Long goodsId);

    @Select("select goods_detail,goods_title,user_id,of.goods_name,of.goods_price,of.goods_count,create_date,status\n" +
            "from order_info of,goods g\n" +
            "where of.goods_id=g.id and of.id=#{orderId}")
    OrderDetailVo getOrderDetailByOrderId(Long orderId);
}
