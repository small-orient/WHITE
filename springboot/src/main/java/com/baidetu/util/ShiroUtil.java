package com.baidetu.util;

import com.baidetu.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * 账户验证
 */
public class ShiroUtil {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
