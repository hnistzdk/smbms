package com.zdk.service.role;

import com.zdk.dao.BaseDao;
import com.zdk.dao.role.RoleDao;
import com.zdk.dao.role.RoleDaoImpl;
import com.zdk.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zdk
 * @date 2021/3/21 16:33
 */
public class RoleServiceImpl implements RoleService{

    private RoleDao roleDao;

    public RoleServiceImpl() {
        this.roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {
        List<Role> roleList=new ArrayList<>();
        Connection connection = null;
        try {

            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResources(connection, null, null);
        }
        return roleList;
    }
}
