package com.dgut.springboot.controller;

import com.dgut.springboot.bean.OrderInfo;
import com.dgut.springboot.bean.SeckillOrder;
import com.dgut.springboot.bean.User;
import com.dgut.springboot.rabbitmq.MQSender;
import com.dgut.springboot.rabbitmq.SeckillOrderMsg;
import com.dgut.springboot.redis.GoodsKey;
import com.dgut.springboot.redis.RedisService;
import com.dgut.springboot.result.CodeMsg;
import com.dgut.springboot.result.Result;
import com.dgut.springboot.service.GoodsService;
import com.dgut.springboot.service.OrderService;
import com.dgut.springboot.service.SeckillService;
import com.dgut.springboot.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SeckillController implements InitializingBean {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @Autowired
    SeckillService seckillService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQSender sender;


    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.getGoodsList();
        if(goodsVoList==null){
            return;
        }
        for(GoodsVo goodsVo : goodsVoList){
            redisService.set(GoodsKey.getGoodsStock,String.valueOf(goodsVo.getId()),goodsVo.getStockCount());
        }
    }

/*
*   QPS:184
* */
    @RequestMapping("/do_kill")
    public String do_kill(Model model , User user, @RequestParam("goodsId") Integer goodsId){
        System.out.println(user+"--"+goodsId);
        if(user == null){
            return "login";
        }
        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
//        查看秒杀库存还有多少
        int stock = goodsVo.getStockCount();
        if(stock<=0){
            model.addAttribute("goodsId",goodsVo.getId());
            model.addAttribute("error_msg",CodeMsg.NO_STOCK);
            return "kill_fail";
        }
//        判断是否已经秒杀了,判断有没该用户的订单
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
//            已经秒杀过了
            model.addAttribute("goodsId",goodsVo.getId());
            model.addAttribute("error_msg",CodeMsg.NO_REPEAT_KILL);
            return "kill_fail";
        }
//        减库存，下订单，创建秒杀订单
        OrderInfo orderInfo = seckillService.killGoods(user,goodsVo);

        model.addAttribute("goods",goodsVo);
        model.addAttribute("order",orderInfo);
        return "order_detail";
    }

/*
*   返回0表示库存没了，返回-1表示秒杀失败，返回orderId表示秒杀成功
*   QPS:292.7
* */
    @RequestMapping("/do_killStatic")
    @ResponseBody
    public Result<Integer> do_killStatic(User user, @RequestParam("goodsId") Integer goodsId){
        System.out.println(user+"--"+goodsId);
        if(user == null){
            return Result.error(CodeMsg.EMAIL_ERROR);
        }
//      在缓存中减库存
        long remainStock = redisService.decr(GoodsKey.getGoodsStock,""+goodsId);
        if(remainStock<0){
            return Result.error(CodeMsg.NO_STOCK);
        }

//        判断是否已经秒杀了,判断有没该用户的订单
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
//            已经秒杀过了
            return Result.error(CodeMsg.NO_REPEAT_KILL);
        }

        SeckillOrderMsg msg = new SeckillOrderMsg();
        msg.setUser(user);
        msg.setGoodsId(goodsId);
//        如果正常则入队秒杀
        sender.sendSeckillOrderMsg(msg);

        return Result.success(0);

        /**
        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
//        查看秒杀库存还有多少
        int stock = goodsVo.getStockCount();
        if(stock<=0){
            return Result.error(CodeMsg.NO_STOCK);
        }
//        判断是否已经秒杀了,判断有没该用户的订单
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
//            已经秒杀过了
            return Result.error(CodeMsg.NO_REPEAT_KILL);
        }
//        减库存，下订单，创建秒杀订单
        OrderInfo orderInfo = seckillService.killGoods(user,goodsVo);
        return Result.success(orderInfo);
         **/
    }

    @RequestMapping("/getSecKillResult")
    @ResponseBody
    public Result<Long> getSecKillResult(User user ,@RequestParam("goodsId") Integer goodsId){
        System.out.println(user+"--"+goodsId);
        if(user == null){
            return Result.error(CodeMsg.EMAIL_ERROR);
        }
        Long result = seckillService.getSeckillResult(user,goodsId);
        return Result.success(result);
    }

    @RequestMapping("/reset")
    @ResponseBody
    public String reset(){
        List<GoodsVo> goodsVos = goodsService.getGoodsList();
        for(GoodsVo goodsVo : goodsVos){
            goodsVo.setStockCount(10);
            redisService.set(GoodsKey.getGoodsStock,""+goodsVo.getStockCount(),10);
            //修改数据库库存信息，
            goodsService.setGoodsStrock(goodsVo.getId());
//            删除是否还有库存的键
            redisService.delete(GoodsKey.getGoodsIsOver,""+goodsVo.getId());
        }
//        和清空订单列表
        goodsService.delOrderTableInfo();
        return null;
    }



}
