package com.zdk.dao.role;

import com.zdk.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author zdk
 * @date 2021/3/21 16:28
 */
public interface RoleDao {
    public List<Role> getRoleList(Connection connection)throws SQLException;

}
