package com.zdk.dao.user;

import com.zdk.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author zdk
 * @date 2021/3/20 14:56
 */
public interface UserDao {
    public User getLoginUser(Connection connection,String userCode)throws SQLException;
    public int modifyPassword(Connection connection,String userCode,String password)throws SQLException;
    public int getUserCount(Connection connection,String userName,int userRole)throws SQLException;
    public List<User> getUserList(Connection connection,String userName,int userRole,int currentPageNo,int pageSize)throws Exception;
    public int addUser(Connection connection,User user)throws Exception;
    public boolean ucExist(String userCode);
    public User userView(int userId);
    public boolean modifyUser(int userId, String userName, int gender, Date birthday,
                              String phone, String address, int userRole);
}
