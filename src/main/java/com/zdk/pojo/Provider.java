package com.zdk.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zdk
 * @date 2021/3/20 13:56
 */
@Data
public class Provider {
    private int id;
    private String proCode;
    private String proName;
    private String proDesc;
    private String proContact;
    private String proPhone;
    private String proAddress;
    private String proFax;
    private int createdBy;
    private Date creationDate;
    private int modifyBy;
    private Date modifyDate;
}
