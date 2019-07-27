
    $("#submitBtn").click(function () {
        var salt = "1a2b3c4d"
        var password = $("#exampleInputPassword").val();
        if(password.length>0) {
            password = salt.charAt(2) + salt.charAt(3) + password + salt.charAt(4) + salt.charAt(0)
            password = $.md5(password)
        }
        $.ajax({
            url: "/do_login",
            type: "POST",
            data: {
                email: $("#exampleInputEmail1").val(),
                password: password
            },
            success: function(data){
                console.log(data)
                if(data.code==0){
                    $("#myModalLabel").text("恭喜！")
                    $("#modalTest").removeClass("alert-danger")
                    $("#modalTest").addClass("alert-success")
                    $("#modalTest").text("登陆成功！")
                    $("#myModal").modal("show")
                    setTimeout("window.location.href='/good_list'", 1000)
                }else {
                    $("#myModalLabel").text("出错啦！")
                    $("#modalTest").removeClass("alert-success")
                    $("#modalTest").addClass("alert-danger")
                    var errorMsg = data.msg;
                    $("#modalTest").text(errorMsg)
                    $("#myModal").modal("show")
                }
            },
            error: function(data){
                console.log(2)
                console.log(data)
            }

        })
    })
