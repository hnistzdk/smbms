package com.zdk.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zdk
 * @date 2021/3/20 13:31
 */
@Data
public class User {
    private int id;
    private String userCode;
    private String userName;
    private String userPassword;
    private int gender;
    private Date birthday;
    private String phone;
    private String address;
    private int userRole;
    private int createdBy;
    private Date creationDate;
    /**
     *更新者和更新时间
     */
    private int modifyBy;
    private Date modifyDate;
    private int age;
    /**
     *用户角色名称
     */
    private String userRoleName;
    public int getAge() {
        Date date=new Date();
        return date.getYear()-birthday.getYear();
    }
}
