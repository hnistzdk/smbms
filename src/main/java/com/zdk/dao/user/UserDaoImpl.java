package com.zdk.dao.user;

import com.zdk.dao.BaseDao;
import com.zdk.pojo.User;
import com.zdk.util.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author zdk
 * @date 2021/3/20 15:07
 */
public class UserDaoImpl implements UserDao {
    @Override
    public User getLoginUser(Connection connection, String userCode) throws SQLException {

        User user=null;
        if(connection!=null){
            String sql="select * from smbms_user where userCode=?";
            Object[] params={userCode};
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=BaseDao.executeQuery(connection,sql, params,preparedStatement);
            if(resultSet.next()){
                user=new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setUserPassword(resultSet.getString("userPassword"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setCreatedBy(resultSet.getInt("createdBy"));
                user.setCreationDate(resultSet.getDate("creationDate"));
                user.setModifyBy(resultSet.getInt("modifyBy"));
                user.setModifyDate(resultSet.getDate("modifyDate"));
            }
            BaseDao.closeResources(null, preparedStatement,resultSet);
        }
        return user;
    }

    @Override
    public int modifyPassword(Connection connection, String userCode,String password)throws SQLException {
        int updateRows=0;
        if(connection!=null){
            String sql="update smbms_user set userPassword=? where userCode=?";
            Object[] params={password,userCode};
            PreparedStatement preparedStatement = null;
            updateRows=BaseDao.executeUpdate(connection, sql, params, preparedStatement);
            BaseDao.closeResources(null, preparedStatement,null);
        }
        return updateRows;
    }

    //根据用户名或角色类型查询用户总数
    @Override
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        int count=0;
        if(connection!=null){
            StringBuffer sql=new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole=r.id");
            ArrayList<Object> list = new ArrayList<>();

            if(userName!=null){
                //模糊查询  带有输入的字符的都能查到
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole>0){
                sql.append(" and u.userRole= ?");
                list.add(userRole);
            }
            Object[] params = list.toArray();
            System.out.println("完整的sql语句为："+sql.toString());

            resultSet=BaseDao.executeQuery(connection, sql.toString(), params, preparedStatement);
            if(resultSet.next()){
                //从结果集中获取数量
                count=resultSet.getInt("count");
            }
            BaseDao.closeResources(null, preparedStatement,resultSet);
        }
        return count;
    }

    //分页查询
    @Override
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        List<User> userList = new ArrayList<>();
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        if(connection!=null){
            StringBuffer sql=new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole =r.id");
            //用于存放参数
            List<Object> list=new ArrayList<>();
            if(userName!=null){
                sql.append(" and u.userName like ?");
                list.add("%"+userName+"%");
            }
            if(userRole>0){
                sql.append(" and u.userRole=?");
                list.add(userRole);
            }

            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo=(currentPageNo-1)*Constants.PAGE_SIZE;
            list.add(currentPageNo);
            list.add(Constants.PAGE_SIZE);

            Object[] params=list.toArray();
            resultSet=BaseDao.executeQuery(connection, sql.toString(), params, preparedStatement);
            while(resultSet.next()){
                User user=new User();
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setUserRoleName(resultSet.getString("userRoleName"));
                userList.add(user);
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return userList;
    }

    @Override
    public int addUser(Connection connection, User user) throws Exception {
        int count=0;
        if(connection!=null){
            String sql="insert into smbms_user values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement=null;
            Object[] params={user.getId(),user.getUserCode(),user.getUserName(),user.getUserPassword()
                    , user.getGender(),user.getBirthday(),user.getPhone(),user.getAddress()
                    , user.getUserRole(),user.getCreatedBy(),user.getCreationDate(),
                    user.getModifyBy(),user.getModifyDate()};
            count=BaseDao.executeAdd(connection,sql,params,preparedStatement);
            BaseDao.closeResources(null, preparedStatement, null);
        }
        System.out.println("添加的数量="+count);
        return count;
    }

    @Override
    public boolean ucExist(String userCode) {
        boolean flag=false;
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        String sql="select * from smbms_user where userCode=?";
        Object[] params={userCode};
        if(userCode!=null){
            connection=BaseDao.getConnection();
            try {
                resultSet=BaseDao.executeQuery(connection, sql, params, preparedStatement);
                if(resultSet.next()){
                    flag=true;
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }finally {
                BaseDao.closeResources(connection, null, resultSet);
            }
        }
        System.out.println(flag);
        return flag;
    }

    @Override
    public User userView(int userId) {
        User user=new User();
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        String sql="select * from smbms_user where id=?";
        Object[] params={userId};
        Map<Integer,String> role = new HashMap<>();
        role.put(1,"系统管理员");role.put( 2,"经理");role.put( 3,"普通员工");
        connection=BaseDao.getConnection();
        try {
            resultSet=BaseDao.executeQuery(connection, sql, params, preparedStatement);
            while (resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setUserRoleName(role.get(resultSet.getInt("userRole")));
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            BaseDao.closeResources(connection, null, resultSet);
        }
        return user;
    }

    @Override
    public boolean modifyUser(int userId, String userName, int gender, Date birthday, String phone, String address, int userRole) {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        String sql="update smbms_user set userName=?,gender=?,birthday=?,phone=?," +
                "address=?,userRole=? where id=?";
        Object[] params={userName,gender,birthday,phone,address,userRole,userId};
        int count=0;
        try {
            connection=BaseDao.getConnection();
            count=BaseDao.executeUpdate(connection, sql, params, preparedStatement);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            BaseDao.closeResources(connection, null, null);
        }
        return count>0;
    }
}


