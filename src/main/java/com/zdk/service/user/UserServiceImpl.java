package com.zdk.service.user;

import com.zdk.dao.BaseDao;
import com.zdk.dao.user.UserDaoImpl;
import com.zdk.dao.user.UserDao;
import com.zdk.pojo.User;

import javax.swing.text.StyledEditorKit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author zdk
 * @date 2021/3/20 15:33
 */
public class UserServiceImpl implements UserService{
    private UserDao userDao;
    public UserServiceImpl(){
        userDao=new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection=null;
        User user=null;
        try {
            connection= BaseDao.getConnection();
            user=userDao.getLoginUser(connection, userCode);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            BaseDao.closeResources(connection,null,null);
        }
        return user;
    }

    @Override
    public boolean modify(String userCode, String password)throws SQLException {
        boolean flag=false;
        Connection connection=BaseDao.getConnection();
        if(userDao.modifyPassword(connection,userCode,password)>0){
            flag=true;
        }
        BaseDao.closeResources(connection, null, null);
        return flag;
    }

    //查询记录数
    @Override
    public int getUserCount(String userName, int userRole) {
        Connection connection=null;
        int count=0;
        try {
            connection = BaseDao.getConnection();
            count=userDao.getUserCount(connection, userName, userRole);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            BaseDao.closeResources(connection, null, null);
        }
        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection=BaseDao.getConnection();
        List<User> userList=new ArrayList<>();
        try {
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public boolean addUser(User user) throws Exception {
        Connection connection=null;
        int count=0;
        if(user!=null){
            connection=BaseDao.getConnection();
            count=userDao.addUser(connection,user);
            BaseDao.closeResources(connection, null,null);
        }
        return count > 0;
    }

    //判断userCode是否存在
    @Override
    public boolean ucExist(String userCode) {
        return userDao.ucExist(userCode);
    }

    //返回查找到的user
    @Override
    public User userView(int userId) {
        return userDao.userView(userId);
    }


    @Override
    public boolean modifyUser(int userId,String userName,int gender,Date birthday,
                           String phone,String address,int userRole) {

        return userDao.modifyUser(userId,userName,gender,birthday, phone,address,userRole);
    }
}
