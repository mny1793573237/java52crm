layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    //监听提交
    form.on('submit(login)', function(data){
        var fieldData=data.field;

        // 判断参数是否为空
        if (fieldData.username == "undefined" || fieldData.username.trim() =="") {
            layer.msg("用户名称不能为空！");
            return false;
        }
        if (fieldData.password == "undefined" || fieldData.password.trim() == "") {
            layer.msg("用户密码不能为空！");
            return false;
        }
        // 发送 ajax 请求，请求用户登录
        $.ajax({
            type:"post",
            url:ctx+"/user/login",
            data:{
                userName:fieldData.username,
                userPwd:fieldData.password
            },
            dataType:"json",
            success:function(msg){
                // 判断是否登录成功
                if (msg.code == 200) {
                    layer.msg("登录成功！", function () {
                        // 将用户信息存到cookie中
                        /*var result = data.result;*/
                        $.cookie("userIdStr", msg.result.userIdStr);
                        $.cookie("userName", msg.result.userName);
                        $.cookie("trueName", msg.result.trueName);

                        //判断是否选中记住我
                        if($("input[type='checkbox']").is(":checked")){
                            $.cookie("userIdStr",msg.result.userIdStr,{expires:7});
                            $.cookie("userName",msg.result.userName,{expires:7});
                            $.cookie("trueName",msg.result.trueName,{expires:7});
                        }
                        // 登录成功后，跳转到首页
                        window.location.href = ctx + "/main";
                    });
                } else {
                    // 提示信息
                    layer.msg(msg.msg);
                }
            }
        });
        // 阻止表单跳转
        return false;

    });
});