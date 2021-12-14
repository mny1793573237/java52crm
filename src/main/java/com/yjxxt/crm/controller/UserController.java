package com.yjxxt.crm.controller;

import com.yjxxt.crm.base.BaseController;
import com.yjxxt.crm.base.ResultInfo;
import com.yjxxt.crm.bean.User;
import com.yjxxt.crm.exceptions.ParamsException;
import com.yjxxt.crm.model.UserModel;
import com.yjxxt.crm.query.UserQuery;
import com.yjxxt.crm.service.UserService;
import com.yjxxt.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo userLogin (String userName, String userPwd){
        ResultInfo resultInfo = new ResultInfo();
       /* try{*/
            // 调用Service层的登录方法，得到返回的用户对象
            UserModel userModel = userService.userLogin(userName, userPwd);
            /**
             * 登录成功后，有两种处理：
             * 1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端
             需要重复登录 ）
             * 2. 将用户信息返回给客户端，由客户端（Cookie）保存
             */
            // 将返回的UserModel对象设置到 ResultInfo 对象中
            resultInfo.setResult(userModel);
        /*}catch(ParamsException e){
            e.printStackTrace();
            // 设置状态码和提示信息
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());

        }catch(Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
        }*/
        return resultInfo;
    }

    @RequestMapping("setting")
    @ResponseBody
    public ResultInfo sayUpdate(User user) {
        ResultInfo resultInfo = new ResultInfo();
        //修改信息
        userService.updateByPrimaryKeySelective(user);
        //返回目标数据对象
        return resultInfo;
    }
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(User user) {
        ResultInfo resultInfo = new ResultInfo();
        userService.addUser(user);
        //返回目标数据对象
        return success("用户添加成功");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(User user) {
        ResultInfo resultInfo = new ResultInfo();
        userService.changeUser(user);
        //返回目标数据对象
        return success("用户修改成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer [] ids) {
        ResultInfo resultInfo = new ResultInfo();
        userService.removeUserIds(ids);
        //返回目标数据对象
        return success("批量删除ok");
    }

    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req) {
        //获取用户的ID
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        //调用方法
        User user = userService.selectByPrimaryKey(userId);
        //存储
        req.setAttribute("user", user);
        //转发
        return "user/setting";
    }

    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req, String oldPassword, String newPassword, String confirmPwd) {
        System.out.println(oldPassword);
        System.out.println(newPassword);
        System.out.println(confirmPwd);
        ResultInfo resultInfo = new ResultInfo();
        // 获取userId
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        // 通过try catch捕获Service层抛出的异常
       /* try {*/
            // 调用Service层的密码修改方法
            userService.changeUserPwd(userId, oldPassword, newPassword, confirmPwd);
        /*} catch (ParamsException e) { // 自定义异常
            e.printStackTrace();
            // 设置状态码和提示信息
            resultInfo.setCode(e.getCode());
            resultInfo.setMsg(e.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
        }*/
        return resultInfo;
    }

    @RequestMapping("sales")
    @ResponseBody
    public List<Map<String, Object>> findSales() {
        List<Map<String, Object>> list = userService.querySales();
        return list;
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> list(UserQuery userQuery) {
        return userService.findUserByParams(userQuery);
    }

    @RequestMapping("index")
    public String index() {
        return "user/user";
    }

    @RequestMapping("addOrUpdatePage")
    public String addOrUpdatePage(Integer id, Model model) {
        if(id!=null){
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }


}
