package com.baidetu.shiro;

import lombok.Data;

import java.io.Serializable;

/**
 * 账户信息类，用于存至浏览器
 */
@Data
public class AccountProfile implements Serializable {


    private Integer id;


    private String userName;


    private String nickName;


    private String email;

}
