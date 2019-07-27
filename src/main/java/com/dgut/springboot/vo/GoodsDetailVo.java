package com.dgut.springboot.vo;

import com.dgut.springboot.bean.User;

public class GoodsDetailVo {
    //        判断活动是否开始了
    int activityStatus = 0;
    //        判断活动还有多长时间
    int remainTime = -1;
    GoodsVo goodsVo;
    User user;

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    public GoodsVo getGoodsVo() {
        return goodsVo;
    }

    public void setGoodsVo(GoodsVo goodsVo) {
        this.goodsVo = goodsVo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
