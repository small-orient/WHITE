package com.baidetu.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 校验用户修改密码实体类
 */
@Data
public class UserPwd implements Serializable {

    private Integer userId; //用户Id
    private String phone; //手机号
    private String smsCode; //验证码
    private String newPassword; //新密码

}
