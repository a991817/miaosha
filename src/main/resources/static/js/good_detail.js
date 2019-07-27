var timer = null
$(function () {
    getDetail();
    // timeDown()

})

function getSeckillResult() {
    var goodsId = $("#goodsId").val()
    $.ajax({
        url:"/getSecKillResult",
        type:"GET",
        data:{
            goodsId:goodsId
        },
        success:function (data) {
            if(data.code==0){
                var result = data.data;
                if(result==0){
                    showModelTip(true,"排队中")
                    setTimeout(function () {
                        getSeckillResult()
                    },50)

                }else if( result == -1){
                    showModelTip(true,"秒杀失败：库存空了！")
                }else{
                    console.log("秒杀成功了")
                    $("#myModal").modal("hide")
                    $("#myModalLabel").text("恭喜！")
                    $("#modalTest").removeClass("alert-danger")
                    $("#modalTest").addClass("alert-success")
                    $("#modalTest").text("秒杀成功！")
                    $("#myModal").modal("show")
                    setTimeout(function () {
                        window.location.href="/order_detail.htm?orderId="+result
                    }, 1000)
                }

            }else{
                showModelTip(true,data.msg)
            }
        },
        error: function () {
            console.log("客户端出错")
        }
    })
}

function doKill() {
    var goodsId = $("#goodsId").val()
    $.ajax({
        url:"/do_killStatic",
        type:"GET",
        data:{
            goodsId:goodsId
        },
        success:function (data) {
            if(data.code==0){
                getSeckillResult()
                // window.location.href="/order_detail.htm?orderId="+data.data.id

            }else{
                showModelTip(true,data.msg)
                // $("#myModalLabel").text("出错啦！")
                // $("#modalTest").removeClass("alert-success")
                // $("#modalTest").addClass("alert-danger")
                // var errorMsg = data.msg;
                // $("#modalTest").text(errorMsg)
                // $("#myModal").modal("show")
            }
        },
        error: function () {
            console.log("客户端出错")
        }
    })
}

function showModelTip(error,msg) {
    //如果是出错信息
    if(msg) {
        $("#myModalLabel").text("出错啦！")
        $("#modalTest").removeClass("alert-success")
        $("#modalTest").addClass("alert-danger")
    }
    $("#modalTest").text(msg)
    $("#myModal").modal("show")
}


function render(goodsDetail) {
    var activityStatus = goodsDetail.activityStatus
    var remainTime = goodsDetail.remainTime;
    var goodsVo = goodsDetail.goodsVo;
    var user = goodsDetail.user;
    $("#goodsTitle").text(goodsVo.goodsTitle)
    $("#goodsDetail").text(goodsVo.goodsDetail)
    $("#startDate").text(new Date(goodsVo.startDate).format("yyyy-MM-dd hh:mm:ss"))
    $("#endDate").text(new Date(goodsVo.endDate).format("yyyy-MM-dd hh:mm:ss"))
    $("#goodsImg").attr("src",goodsVo.goodsImg)
    if(activityStatus==0){
        $("#noBeginDiv").removeAttr("hidden")
    }else if(activityStatus==1){
        $("#statusDiv").html("<div class=\"alert alert-info\" role=\"alert\">秒杀已经开始</div>")
    }else {
        $("#statusDiv").html("<div class=\"alert alert-warning\" role=\"alert\">秒杀已经结束</div>")
    }
    $("#goodsId").val(goodsVo.goodsId)
    $("#remainTimeSpan").text(remainTime)
    $("#remainTime").val(remainTime)
    timeDown()
}

function getDetail() {
    var goodsId = g_getQueryString("goodsId")
    $.ajax({
        url:"/goodsDetailStatic/"+goodsId,
        type: "GET",
        success: function (data) {
            if(data.code==0){
                console.log(data.data)
                render(data.data)
            }else{
                console.log("服务端错误:"+data.msg)
            }
        },
        error: function () {
            console.log("客户端请求错误")
        }
    })
}

function timeDown() {
    var remainTime = $("#remainTime").val()
    var date = SecondToDate(remainTime)
    $("#remainTimeSpan").text(date);
    if(remainTime == 0){
        //活动开始了
        clearTimeout(timer);
        $("#secKillBtn").attr("disabled",false);
        $("#statusDiv").html("<div class=\"alert alert-info\" role=\"alert\" th:if=\"${activityStatus eq 1}\">秒杀已经开始</div>")
    }
    if(remainTime > 0){
        // 活动还没开始
        timer = setTimeout(function () {
            $("#remainTime").val(remainTime - 1)
            timeDown()
        },1000)
        $("#secKillBtn").attr("disabled",true);
    }else if(remainTime<-1){
        //    活动结束了
        console.log(remainTime)
        $("#secKillBtn").attr("disabled",true);
    }

}
//把秒转换成日时秒
function SecondToDate(msd) {
    var time =msd
    if (null != time && "" != time) {
        if (time > 60 && time < 60 * 60) {
            time = parseInt(time / 60.0) + "分钟" + parseInt((parseFloat(time / 60.0) -
                parseInt(time / 60.0)) * 60) + "秒";
        }
        else if (time >= 60 * 60 && time < 60 * 60 * 24) {
            time = parseInt(time / 3600.0) + "小时" + parseInt((parseFloat(time / 3600.0) -
                parseInt(time / 3600.0)) * 60) + "分钟" +
                parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
                    parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + "秒";
        } else if (time >= 60 * 60 * 24) {
            time = parseInt(time / 3600.0/24) + "天" +parseInt((parseFloat(time / 3600.0/24)-
                parseInt(time / 3600.0/24))*24) + "小时" + parseInt((parseFloat(time / 3600.0) -
                parseInt(time / 3600.0)) * 60) + "分钟" +
                parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) -
                    parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + "秒";
        }
        else {
            time = parseInt(time) + "秒";
        }
    }
    return time;
}


//点击秒杀