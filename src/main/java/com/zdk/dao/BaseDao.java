package com.zdk.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author zdk
 * @date 2021/3/20 12:33
 * 操作数据库的公共类
 */
public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    //静态代码块  类加载的时候就会初始化
    static {
        //通过类加载器读取对应的资源
        InputStream is=BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        Properties properties=new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver=properties.getProperty("driver");
        url=properties.getProperty("url");
        username=properties.getProperty("username");
        password=properties.getProperty("password");

    }

    //获取数据库的连接
    public static Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName(driver);
            connection=DriverManager.getConnection(url, username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 增
     */
    public static int executeAdd(Connection connection, String sql, Object[] params,PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1, params[i]);
        }
        return preparedStatement.executeUpdate();
    }
    /**
     * 删
     */
    public static int executeDelete(Connection connection, String sql, Object[] params,PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1, params[i]);
        }
        return preparedStatement.executeUpdate();
    }

    /**
     * 改
     */
    public static int executeUpdate(Connection connection, String sql, Object[] params,PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1, params[i]);
        }
        //返回更新的行数
        return preparedStatement.executeUpdate();
    }

    /**
     * 查
     */
    public static ResultSet executeQuery(Connection connection, String sql, Object[] params,PreparedStatement preparedStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i+1, params[i]);
        }
        return preparedStatement.executeQuery();
    }

    /**
     * 释放资源 返回true or false
     */
    public  static boolean closeResources(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet){
        boolean flag=true;
        if(resultSet!=null){
            try {
                resultSet.close();
                //使为空是为了防止关闭失败，为空可让垃圾回收机制处理
                resultSet=null;
            }catch (Exception e){
                e.printStackTrace();
                flag=false;
            }
        }
        if(preparedStatement!=null){
            try {
                preparedStatement.close();
                preparedStatement=null;
            }catch (Exception e){
                e.printStackTrace();
                flag=false;
            }
        }
        if(connection!=null){
            try {
                connection.close();
                connection=null;
            }catch (Exception e){
                e.printStackTrace();
                flag=false;
            }
        }
        return flag;
    }
}
