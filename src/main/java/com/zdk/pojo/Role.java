package com.zdk.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zdk
 * @date 2021/3/20 14:02
 */
@Data
public class Role {
    private int id;
    private String roleCode;
    private String roleName;
    private int createdBy;
    private Date creationDate;
    private int modifyBy;
    private Date modifyDate;
}
