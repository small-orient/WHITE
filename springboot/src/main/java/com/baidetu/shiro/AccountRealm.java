package com.baidetu.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.baidetu.entity.HUser;
import com.baidetu.service.HUserService;
import com.baidetu.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * shiro realm
 */
@Component
public class AccountRealm extends AuthorizingRealm {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    HUserService userService;

    /**
     * 判断获得的token是否属于jwt的token
     *
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }


    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //当判断为jwt的token后，直接强转
        JwtToken jwtToken = (JwtToken) token;
        //校验
        Claims claim = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal());
        //获取存入的userId信息
        String userId = claim.getSubject();
        //查询数据库
        HUser user = userService.getById(Integer.parseInt(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }

        if (user.getLocked() == -1) {
            throw new LockedAccountException("账户被锁定");
        }

        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user, profile);

        /*System.out.println(jwtToken.getCredentials());
        System.out.println(jwtToken.getCredentials().toString());
        System.out.println(blogger.getPassword());*/

        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());

    }
}
