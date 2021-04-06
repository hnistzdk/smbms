package com.zdk.service.user;

import com.zdk.pojo.Role;
import com.zdk.pojo.User;
import sun.util.resources.cldr.es.CalendarData_es_PY;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @author zdk
 * @date 2021/3/20 15:31
 */
public interface UserService {
    //用户登录
    public User login(String userCode,String password);
    public boolean modify(String userCode, String password)throws SQLException;
    public int getUserCount(String userName,int userRole);
    public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo,int pageSize);
    public boolean addUser(User user)throws Exception;
    public boolean ucExist(String userCode);
    public User userView(int userId);
    public boolean modifyUser(int userId, String userName, int gender, Date birthday,
                           String phone, String address, int userRole);

}
