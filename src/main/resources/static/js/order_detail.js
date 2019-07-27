function render(data){
    $("#goodsTitle").text(data.goodsTitle)
    $("#goodsDetail").text(data.goodsDetail)
    $("#userId").text("用户名："+data.userId)
    $("#goodsName").text("商品名："+data.goodsName)
    $("#goodsPrice").text("商品价格："+data.goodsPrice)
    $("#goodsCount").text("购买数量："+data.goodsCount)
    $("#createDate").text("订单创建时间："+new Date(data.createDate).format("yyyy-MM-dd hh:mm:ss"))
    $("#orderStatus").val(data.status)
}

$(function () {
    var orderId = g_getQueryString("orderId")
    console.log(orderId)
    $.ajax({
        url:"order_detail_body?orderId="+orderId,
        type:"GET",
        success:function (data) {
            console.log(data)
            render(data.data)
        },
        error:function () {
            console.log("ajax 请求出错")
        }
    })


    var status = $("#orderStatus").val();
    var statusText = "下单状态：";
    if(status==0) {
        statusText += "新建未支付"
    }else if(status==1){
        statusText+="已支付"
    }else if(status==2){
        statusText+="已发货"
    }else if(status==3){
        statusText+="已收货"
    }else if(status==4){
        statusText+="已退款"
    }else if(status==5){
        statusText+="已完成"
    }
    $("#statusDiv").text(statusText)
})