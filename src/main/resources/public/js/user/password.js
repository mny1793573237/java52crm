layui.use(['form','jquery','jquery_cookie','layer'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);


    //监听提交
    form.on('submit(saveBtn)', function(data){
        var fieldData=data.field;


        $.ajax({
            type: "post",
            url: ctx+"/user/updatePwd",
            data: {
                "oldPassword":fieldData.old_password,
                "newPassword":fieldData.new_password,
                "confirmPwd":fieldData.again_password
            },
            dataType: "json",
            success:function(msg){
                if (msg.code == 200) {
                    layer.msg("修改密码成功了，系统将在三秒后退出！", function () {
                        $.removeCookie("userIdStr",{domain:"localhost",path:"/crm"});
                        $.removeCookie("userName",{domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName",{domain:"localhost",path:"/crm"});
                        window.parent.location.href = ctx + "/index";
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