package com.baidetu.controller;

import com.baidetu.common.Result;
import com.baidetu.common.dto.Code;
import com.baidetu.common.dto.UserPwd;
import com.baidetu.entity.HUser;
import com.baidetu.service.HUserService;
import com.baidetu.service.SmsCodeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 手机验证码controller
 */
@RestController
@CrossOrigin //跨域
@RequestMapping("/sms")
public class SmsCodeController {

    @Autowired
    private SmsCodeService smsCodeService;

    @Resource
    private HUserService userService;

    private Code scode = new Code();//验证码

    //获取手机号，发送验证码
    @PostMapping("/send")
    public Result code(@RequestBody String phone) {
        //验证码是否在缓存
        Jedis jedis = new Jedis();
        String code = jedis.get(phone);
        if (!StringUtils.isEmpty(code)) {
            return Result.success(phone + ":验证码已存在，且未过期");
        }
        //缓存没有
        code = UUID.randomUUID().toString().substring(0, 4);
        //存至全局验证码变量
        scode.setSmsCode(code);
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        boolean isSend = smsCodeService.send(phone, "SMS_205449883", param);
        if (isSend) {
            //缓存至redis
            jedis.setex(phone, 61, code);
            return Result.success(null);
        }
        return Result.failure("发送失败！");
    }

    /**
     * 用户修改密码
     *
     * @param user
     * @return
     */
    @PostMapping("/updatePwd")
    @RequiresAuthentication
    public Result updatePwd(@RequestBody UserPwd user) {
        Result result = smsCodeService.updatePwd(user, scode.getSmsCode());
        //清空临时验证码
        scode.setSmsCode("");
        return result;
    }

    /**
     * 用户找回密码:校验手机号是否绑定用户
     *
     * @param user
     * @return
     */
    @PostMapping("/retrieve")
    public Result retrieve(@RequestBody UserPwd user) {

        //查询该手机号是否被用户所绑定
        HUser one = userService.getOne(new QueryWrapper<HUser>().eq("phone", user.getPhone()));
        if (one != null) {
            //被绑定,校验验证码
            if (!user.getSmsCode().equals(scode.getSmsCode())) {
                //失败
                return Result.failure("验证码错误！");
            }
            //验证码正确,返回用户基本信息
            user.setUserId(one.getId());
            return Result.success(user);
        } else {
            //查不到
            return Result.failure("该手机号未被绑定！");
        }

    }

    /**
     * 用户找回密码：方法与修改密码一致，但因权限问题不能开放同一个接口，否则接口被直接调用有风险
     *
     * @param user
     * @return
     */
    @PostMapping("/retrievePwd")
    public Result retrievePwd(@RequestBody UserPwd user) {
        Result result = smsCodeService.updatePwd(user, scode.getSmsCode());
        //清空临时验证码
        scode.setSmsCode("");
        return result;
    }

}
