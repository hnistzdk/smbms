package com.zdk.dao.role;

import com.zdk.dao.BaseDao;
import com.zdk.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zdk
 * @date 2021/3/21 16:28
 */
public class RoleDaoImpl implements RoleDao{
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {
        List<Role> roleList=new ArrayList<>();
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        if(connection!=null){
            String sql="select *from smbms_role";
            Object[] params={};
            resultSet= BaseDao.executeQuery(connection, sql, params, preparedStatement);
            while(resultSet.next()){
                Role role=new Role();
                role.setId(resultSet.getInt("id"));
                role.setRoleCode(resultSet.getString("roleCode"));
                role.setRoleName(resultSet.getString("roleName"));
                roleList.add(role);
            }
            BaseDao.closeResources(null, preparedStatement, resultSet);
        }
        return roleList;
    }
}
