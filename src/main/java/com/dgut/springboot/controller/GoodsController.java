package com.dgut.springboot.controller;

import com.dgut.springboot.bean.User;
import com.dgut.springboot.redis.GoodsKey;
import com.dgut.springboot.redis.RedisService;
import com.dgut.springboot.result.Result;
import com.dgut.springboot.service.GoodsService;
import com.dgut.springboot.service.UserServiceImpl;
import com.dgut.springboot.vo.GoodsDetailVo;
import com.dgut.springboot.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
public class GoodsController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GoodsService goodsService;
    @Autowired
    RedisService redisService;
//    用于手动渲染页面
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

/**
 * 第一次
 * GPS-348/SEC
 * 500*20
  */

    @RequestMapping(value = "/good_list",produces = "text/html")
    @ResponseBody
    public String good_list(HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse, Model model, User user){
//        return "good_list";
//        返回一个html页面字符串
//        先从缓存中取
        String html = redisService.get(GoodsKey.getGoodsListPage,"",String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }else {
            model.addAttribute("user",user);
            List<GoodsVo> goodss = goodsService.getGoodsList();
            model.addAttribute("goodsList",goodss);

            WebContext context = new WebContext(httpServletRequest,httpServletResponse,
                    httpServletRequest.getServletContext(),httpServletRequest.getLocale(),
                    model.asMap());
//            第一个参数是模板名称，第二个是容器，可用SpringWebContext
             html = thymeleafViewResolver.getTemplateEngine().process("good_list",context);
             if(!StringUtils.isEmpty(html)){
//                 保存到缓存中
                 redisService.set(GoodsKey.getGoodsListPage,"",html);
             }
             return html;
        }
    }

    /**
     * 152/sec
     * 500*20
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value = "/good_list_noCac")
    public String good_list_good_list_noCac(Model model,User user){
        model.addAttribute("user",user);
        List<GoodsVo> goodss = goodsService.getGoodsList();
        model.addAttribute("goodsList",goodss);
        return "good_list";
    }

    @RequestMapping(value="/good_detail/{id}",produces = "text/html")
    @ResponseBody
    public String goodsDetail(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse,
                              Model model, User user, @PathVariable("id") long id){
        model.addAttribute("user",user);

        GoodsVo goodsVo = goodsService.getGoodsById(id);
        model.addAttribute("goods",goodsVo);

        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = new Date().getTime();
//        判断活动是否开始了
        int activityStatus = 0;
//        判断活动还有多长时间
        int remainTime = -1;

        if(startTime<now && endTime>now){
//        活动已经开始
            activityStatus = 1;
        }else if(now < startTime){
//        活动还没有开始
            activityStatus = 0;
            remainTime = (int) ((startTime - now)/1000);
        }else if(now > endTime){
//        活动已经结束了
            activityStatus = 2;
        }
        model.addAttribute("activityStatus",activityStatus);
        model.addAttribute("remainTime",remainTime);
//        return "good_detail";
        String html = redisService.get(GoodsKey.getGoodsDetailPage,""+id,String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }else {
            WebContext context = new WebContext(httpServletRequest,httpServletResponse,
                    httpServletRequest.getServletContext(),httpServletRequest.getLocale(),
                    model.asMap());
//            第一个参数是模板名称，第二个是容器，可用SpringWebContext
            html = thymeleafViewResolver.getTemplateEngine().process("good_detail",context);
            if(!StringUtils.isEmpty(html)){
//                 保存到缓存中
                redisService.set(GoodsKey.getGoodsDetailPage,""+id,html);
            }
            return html;
        }
    }
    @RequestMapping(value="/goodsDetailStatic/{id}")
    @ResponseBody
    public Result<GoodsDetailVo> goodsDetailStatic(HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse,
                              Model model, User user, @PathVariable("id") long id){
        model.addAttribute("user",user);
        GoodsVo goodsVo = goodsService.getGoodsById(id);
        model.addAttribute("goods",goodsVo);
        long startTime = goodsVo.getStartDate().getTime();
        long endTime = goodsVo.getEndDate().getTime();
        long now = new Date().getTime();
//        判断活动是否开始了
        int activityStatus = 0;
//        判断活动还有多长时间
        int remainTime = -1;
        if(startTime<now && endTime>now){
//        活动已经开始
            activityStatus = 1;
        }else if(now < startTime){
//        活动还没有开始
            activityStatus = 0;
            remainTime = (int) ((startTime - now)/1000);
        }else if(now > endTime){
//        活动已经结束了
            activityStatus = 2;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setActivityStatus(activityStatus);
        goodsDetailVo.setRemainTime(remainTime);
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setUser(user);
        return Result.success(goodsDetailVo);
        }
}
