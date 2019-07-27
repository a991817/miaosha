package com.dgut.springboot.controller;

import com.dgut.springboot.bean.OrderInfo;
import com.dgut.springboot.bean.User;
import com.dgut.springboot.result.Result;
import com.dgut.springboot.service.GoodsService;
import com.dgut.springboot.service.OrderService;
import com.dgut.springboot.vo.GoodsVo;
import com.dgut.springboot.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    OrderService orderService;
    @RequestMapping("/orderDetail")
    public String orderDetail(Model model,User user,@RequestParam("goodsId")long goodsId){
        GoodsVo goodsVo = goodsService.getGoodsById(goodsId);
        OrderInfo orderInfo = orderService.getOrderByUserIdGoodsId(user.getId(),goodsId);
        model.addAttribute("goods",goodsVo);
        model.addAttribute("order",orderInfo);
        return "order_detail";
    }

    @RequestMapping("/order_detail_body")
    @ResponseBody
    public Result<OrderDetailVo> order_detail_body(User user,@RequestParam("orderId")Long orderId){
        System.out.println(orderId);
        OrderDetailVo orderDetailVo = orderService.getOrderDetailByOrderId(orderId);
        return Result.success(orderDetailVo);
    }


}
