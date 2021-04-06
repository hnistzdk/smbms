package com.zdk.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author zdk
 * @date 2021/3/20 13:23
 */

@Data
public class Bill {
    private int id;
    private String billCode;
    private String productName;
    private String productDesc;
    private String productUnit;
    private double productCount;
    private double totalPrice;
    private int isPayment;
    private int createdBy;
    private Date creationDate;
    private int modifyBy;
    private Date modifyDate;
    private int providedId;
    private String providerName;
}
