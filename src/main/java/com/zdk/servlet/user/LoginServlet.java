package com.zdk.servlet.user;

import com.zdk.pojo.User;
import com.zdk.service.user.UserService;
import com.zdk.service.user.UserServiceImpl;
import com.zdk.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zdk
 * @date 2021/3/20 16:02
 */
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userCode=req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");
        /**
        *调用service层 获取user对象  判断是否能获取  不能获取 证明不存在此用户
         * 如果获取到了  判断密码是否正确  正确跳转到相应页面  不正确给出提示
        */
        UserService userService=new UserServiceImpl();
        User user=userService.login(userCode, userPassword);
        if(user!=null){
            //将用户信息放到session中
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            if(userPassword.equals(user.getUserPassword())){
                resp.sendRedirect("jsp/frame.jsp");
            }else{
                req.setAttribute("error", "密码错误");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        }else{
            //没找到 转发回登录页面  提示用户名或密码错误
            req.setAttribute("error", "用户名或密码不正确");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
