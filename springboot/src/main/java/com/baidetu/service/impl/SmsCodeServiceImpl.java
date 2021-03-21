package com.baidetu.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baidetu.common.Result;
import com.baidetu.common.dto.UserPwd;
import com.baidetu.dao.HUserMapper;
import com.baidetu.entity.HUser;
import com.baidetu.service.SmsCodeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 发送短信验证码实现类
 */
@Service
public class SmsCodeServiceImpl implements SmsCodeService {

    @Resource
    private HUserMapper userMapper;

    @Override
    public boolean send(String phoneNum, String templateCode, Map<String, Object> code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                "LTAI4GL8Td93JduKKewdKA29",
                "iKfkWP1Wd2MkGLwwcjcEkNg8gSFaLi");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        //自定义参数(手机号、验证码、签名、短信模板)
        request.putQueryParameter("PhoneNumbers", phoneNum);
        request.putQueryParameter("SignName", "白的兔的小站");
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", new JSONObject(code).toString());
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据用户信息修改密码
     *
     * @param user
     * @param smsCode 验证码
     * @return
     */
    @Transactional
    @Override
    public Result updatePwd(UserPwd user, String smsCode) {

        //查询该手机号是否为该用户id所有
        HUser one = userMapper.selectOne(new QueryWrapper<HUser>().eq("phone", user.getPhone()));
        if (one != null) {
            //判断是否为同一用户所有
            if (one.getId().equals(user.getUserId())) {
                //确认身份
                //判断验证码
                if (!user.getSmsCode().equals(smsCode)) {
                    //失败
                    return Result.failure("验证码错误！");
                }
                //验证码正确,保存新密码
                HUser hUser = new HUser();
                String pwd = SecureUtil.md5(user.getNewPassword());
                hUser.setPassword(pwd);
                hUser.setId(user.getUserId());
                int update = userMapper.updateById(hUser);

                if (update > 0) {
                    return Result.success(null);
                }
            } else {
                return Result.failure("该手机号未被当前账号绑定！");
            }
        } else {
            //查不到
            return Result.failure("该手机号未被绑定！");
        }


        return Result.failure("修改失败，请重试");
    }
}
