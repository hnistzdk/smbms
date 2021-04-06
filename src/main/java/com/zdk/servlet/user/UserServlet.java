package com.zdk.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.sun.javafx.binding.StringFormatter;
import com.zdk.dao.BaseDao;
import com.zdk.dao.user.UserDaoImpl;
import com.zdk.pojo.Role;
import com.zdk.pojo.User;
import com.zdk.service.role.RoleServiceImpl;
import com.zdk.service.user.UserServiceImpl;
import com.zdk.util.Constants;
import com.zdk.util.PageSupport;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zdk
 * @date 2021/3/20 20:29
 *
 * 将不同的操作提取成一个方法  在servlet的doPost doGet方法中判断操作名称，然后用this.调用相应方法 实现servlet的复用
 *
 */
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method=req.getParameter("method");
        if(method!=null&&method.equals("savepwd")){
            this.modifyPassword(req, resp);
        }
        else if(method!=null&&method.equals("pwdmodify")){
            this.oldPwdTrue(req, resp);
        }
        else if(method!=null&&method.equals("query")){
            try {
                this.query(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(method!=null&&method.equals("add")){
            try {
                this.add(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(method!=null&&method.equals("getrolelist")){
            this.getRoleList(req, resp);
        }
        else if(method!=null&&method.equals("ucexist")){
            this.ucExist(req, resp);
        }
        else if(method!=null&&method.equals("view")){
            this.userView(req, resp);
        }
        else if(method!=null&&method.equals("modify")){
            try {
                this.modifyUser(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    //修改旧密码
    public void modifyPassword(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        User user=(User) req.getSession().getAttribute(Constants.USER_SESSION);
        if(user!=null){
            String userCode=user.getUserCode();
            String password=user.getUserPassword();
            String oldPassword=req.getParameter("oldpassword");
            String newPassword=req.getParameter("newpassword");
            boolean flag=false;
            if(!newPassword.equals(oldPassword)&&oldPassword.equals(password)){
                try {
                    flag=new UserServiceImpl().modify(userCode, newPassword);
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
                if(flag){
                    req.setAttribute("message", "密码修改成功,请退出重新登录");
                    //密码修改成功移除当前session  移除后就会自动跳转到登录页面
                    req.getSession().removeAttribute(Constants.USER_SESSION);
                }else {
                    req.setAttribute("message", "密码修改失败");
                }
            }else {
                req.setAttribute("message", "原密码错误，请重新进行操作");
                req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
            }
            //加上下面这句代码  修改成功后并不会自动跳转到登录界面  而不加 则会跳到请求的URL  再次刷新会跳到登录界面  原因未知
            //req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        }
    }

    //验证旧密码
    public void oldPwdTrue(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        User user=(User)req.getSession().getAttribute(Constants.USER_SESSION);
        String oldPassword=req.getParameter("oldpassword");

        //使用万能的map:结果集
        HashMap<String,String> resultMap = new HashMap<>();
        //将resultMap转为JSON   返回给前端Ajax请求的data
        if(user==null){
            //session失效了  或session过期了
            resultMap.put("result", "sessionerror");
        }else if(oldPassword==null){
            //输入的密码为空
            resultMap.put("result", "error");
        }else {
            String userPassword=user.getUserPassword();
            if(oldPassword.equals(userPassword)){
                resultMap.put("result","true");
            }else {
                resultMap.put("result","false");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void query(HttpServletRequest req, HttpServletResponse resp)throws Exception{
        //查询用户列表

        String queryUserName=req.getParameter("queryname");
        String temp=req.getParameter("queryUserRole");
        String pageIndex=req.getParameter("pageIndex");
        int queryUserRole=0;

        //获取用户列表
        UserServiceImpl userService=new UserServiceImpl();
        List<User> userList=null;

        int pageSize=5;
        //第一次为第一页
        int currentPageNo=1;
        if(queryUserName==null){
            queryUserName="";
        }
        if(temp!=null&&!temp.equals("")){
            //给查询赋值
            queryUserRole=Integer.parseInt(temp);
        }
        if(pageIndex!=null){
            currentPageNo=Integer.parseInt(pageIndex);
        }


        //获取用户总数(分页：上一页，下一页的情况)
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);

        //总页数支持
        PageSupport pageSupport=new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        //控制首页和尾页 防错
        int totalPageCount = pageSupport.getTotalPageCount();
        if(totalPageCount<1){
            currentPageNo=1;
        }
        else if(currentPageNo>totalPageCount){
            currentPageNo=totalPageCount;
        }

        //获取用户列表展示
        userList=userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList=roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);


        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }

    public void add(HttpServletRequest req, HttpServletResponse resp)throws Exception{
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<String,Integer> role = new HashMap<>();
        role.put("系统管理员",1);role.put("经理", 2);role.put("普通员工", 3);

        String userCode=req.getParameter("userCode");
        String userName=req.getParameter("userName");
        String userPassword=req.getParameter("userPassword");
        String rUserPassword=req.getParameter("ruserPassword");
        int gender=Integer.parseInt(req.getParameter("gender"));
        Date birthday=ft.parse(req.getParameter("birthday"));
        String phone=req.getParameter("phone");
        String address=req.getParameter("address");
        Integer userRole=role.get(req.getParameter("userRole"));
        int createdBy=1;
        Date creationDate=df.parse(df.format(new Date()));

        User user=new User();
        user.setId((new UserServiceImpl().getUserCount(null, 0))+1);
        user.setUserCode(userCode);user.setUserName(userName);
        user.setUserPassword(userPassword);user.setGender(gender);
        user.setBirthday(birthday);user.setPhone(phone);
        user.setAddress(address);user.setUserRole(3);
        user.setCreatedBy(createdBy);user.setCreationDate(creationDate);
        user.setModifyBy(1);user.setModifyDate(null);

        UserServiceImpl userService=new UserServiceImpl();
        boolean flag=userService.addUser(user);
        System.out.println("flag="+flag);
        if(flag){
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        }
    }

    public void getRoleList(HttpServletRequest req, HttpServletResponse resp){
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList=roleService.getRoleList();
        try {
            resp.setContentType("application/json");
            PrintWriter writer = null;
            writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(roleList));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //异步判断用户编码是否存在
    public void ucExist(HttpServletRequest req, HttpServletResponse resp){
        String userCode=req.getParameter("userCode");
        Map<String, String> map=new HashMap<>();
        UserServiceImpl userService=new UserServiceImpl();
        if(userService.ucExist(userCode)){
            map.put("userCode","exist");
        }else {
            map.put("userCode","notExist");
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = null;
            writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(map));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userView(HttpServletRequest req, HttpServletResponse resp){
        int userId = Integer.parseInt(req.getParameter("uid"));
        UserServiceImpl userService=new UserServiceImpl();
        User user=userService.userView(userId);
        req.setAttribute("user", user);
        try {
            req.getRequestDispatcher("userview.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void modifyUser(HttpServletRequest req, HttpServletResponse resp)throws Exception{

        //先将请求转发到usermodify.jsp页面
        try {
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        String userId = req.getParameter("uid");
        UserServiceImpl userService = new UserServiceImpl();
        boolean flag=userService.modifyUser(Integer.parseInt(userId), userName, Integer.parseInt(gender),
                new SimpleDateFormat("yyyy-MM-dd").parse(birthday),
                phone,address,Integer.parseInt(userRole));

    }

    @Test
    public void test(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(new Date()));
    }
}
