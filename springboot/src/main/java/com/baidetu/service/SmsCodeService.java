package com.baidetu.service;

import com.baidetu.common.Result;
import com.baidetu.common.dto.UserPwd;

import java.util.Map;

/**
 * 短信验证码接口
 */
public interface SmsCodeService {

    //发送验证码
    boolean send(String phoneNum,
                 String templateCode,
                 Map<String, Object> code);

    //根据用户信息修改密码
    Result updatePwd(UserPwd user, String smsCode);
}


