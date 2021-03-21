package com.baidetu.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baidetu.common.Result;
import com.baidetu.common.dto.LoginDto;
import com.baidetu.common.dto.ReplyDto;
import com.baidetu.common.dto.UserInfo;
import com.baidetu.dao.HUserMapper;
import com.baidetu.entity.HUser;
import com.baidetu.service.HUserService;
import com.baidetu.shiro.AccountProfile;
import com.baidetu.util.JwtUtils;
import com.baidetu.util.NotNullUtils;
import com.baidetu.util.ShiroUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 博主登录控制
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/user")
public class HUserController {

    @Resource
    HUserService userService;

    @Resource
    HUserMapper userMapper;
    @Autowired
    JwtUtils jwtUtils;

    //注入服务器IP
    @Value("${server.ip}")
    String serverIp;


    /**
     * 登录校验
     *
     * @param loginDto
     * @param response
     * @return Result
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto
            , HttpServletResponse response) {
        //能进来，说明后端校验成功，username与password不为空了
        System.out.println(loginDto.getUserName() + "---登录日志---" + loginDto.getPassword());
        //查询数据库username与所传参数进行校验
        HUser user = userService.getOne(new QueryWrapper<HUser>()
                .eq("userName", loginDto.getUserName()));


        /**
         * 断言：notNull源码,object为空，则会抛出异常
         *
         *  public static void notNull(@Nullable Object object, String message) {
         *         if (object == null) {
         *             throw new IllegalArgumentException(message);
         *         }
         *     }
         */
        //判断用户是否存在
        Assert.notNull(user, "用户不存在，请确认用户名是否正确");
        //存在，则校验密码
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            //不匹配
            /* return Result.failure("密码不正确"); 不能给的太直白*/
            return Result.failure("账号或密码错误");
        }

        //查看是否被冻结
        if (user.getLocked() == 1)
            return Result.failure("账户已被冻结，请联系博主");

        //密码匹配,生成token
        String jwtToken = jwtUtils.generateToken(user.getId());
        //jwtToken存至头信息
        response.setHeader("Authorization", jwtToken);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        //更新用户登录时间
        user.setLastTime(user.getLoginTime());
        user.setLoginTime(new Date());
        userService.updateById(user);

        //return Result.success(null);
        //为了少写一个回显信息接口，可以直接在这里返回用户的基本信息
        /**
         *
         * hutool工具的MapUtil.builder(),底层源码就是一个map集合.
         *
         * public MapBuilder(Map<K, V> map) {
         *         this.map = map;
         *     }
         *
         */
        //将用户信息存入map返回浏览器
        return Result.success(MapUtil.builder()
                .put("id", user.getId())
                .put("userName", user.getUserName())
                .put("nickName", user.getNickName())
                .put("sign", user.getSign())
                .put("email", user.getEmail())
                .put("avatar", user.getAvatar())
                .map());
    }


    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("/registered")
    public Result registered(@Validated @RequestBody HUser user) {
        //查询用户名是否重复
        HUser userName = userService
                .getOne(new QueryWrapper<HUser>().eq("userName", user.getUserName()));
        if (userName != null) {
            return Result.failure(400, "用户名已存在！", null);
        }

        //查询手机号是否被绑定
        if (NotNullUtils.String(user.getPhone())) {
            HUser phone = userService
                    .getOne(new QueryWrapper<HUser>().eq("phone", user.getPhone()));
            Assert.isNull(phone, "该手机号已被绑定，请更换填写手机号");
        }

        String pwd = SecureUtil.md5(user.getPassword());
        user.setPassword(pwd);

        //不重复，存值
        int insert = userMapper.insert(user);

        if (insert != 0) {
            return Result.success(null);
        }

        return Result.failure("注册失败，请重试");

    }

    /*
     * 上传头像
     * @param file
     * @return
     */
    @PostMapping("/avatar")
    @RequiresAuthentication
    public Result avatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        //从shiro中获取当前用户id
        AccountProfile profile = ShiroUtil.getProfile();
        /* System.out.println(profile.getId()+"--------------userId");*/
        return userService.uploadAvatar(file, profile.getId(), request);
    }


    /*
     * 用户个人资料回显
     *
     * @return
     */
    @GetMapping("/owner")
    @RequiresAuthentication
    public Result owner() {
        //从shiro中获取当前用户id
        AccountProfile profile = ShiroUtil.getProfile();
        HUser user = userService.getById(profile.getId());
        String avatar = user.getAvatar();
        user.setAvatar("https://" + serverIp + avatar);
        /*    user.setAvatar("https://"+serverIp+ avatar);*/
        //存入回显个人资料实体类
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(user, userInfo);

        //防止直接调接口
        if (user.getLocked() == 1)
            return Result.failure("用户已被冻结，操作失败");

        return Result.success(userInfo);
    }

    /*
     * 用户个人资料修改
     * @return
     */
    @PostMapping("/owner/personal")
    @RequiresAuthentication
    public Result personal(@RequestBody UserInfo userInfo) {

        /* System.out.println(userInfo.toString()+"------用户修改个人资料信息--------");*/
        //从shiro中获取当前用户id,与前端对象所给用户id比较，确认是否一致，二次校验
        AccountProfile profile = ShiroUtil.getProfile();
        if (profile.getId().equals(userInfo.getId())) {
            //一致，可修改
            HUser user = userService.getById(profile.getId());

            //防止直接调接口
            if (user.getLocked() == 1)
                return Result.failure("用户已被冻结，操作失败");

            //复制修改信息
            BeanUtil.copyProperties(userInfo, user);
            //更新
            boolean update = userService.updateById(user);
            if (update) {
                return Result.success(null);

            } else {
                return Result.failure("个人资料修改失败，请重试");
            }
        } else {
            //不是一个人，拒绝修改
            return Result.failure("用户信息错误");
        }
    }

    /*
     * 用户管理
     *
     * @return
     */
    @PostMapping("/admin/user")
    @RequiresAuthentication
    public Result adminForUser(@RequestBody String dataJson) {

        JSONObject jsonObject = JSONUtil.parseObj(dataJson);
        Integer currPage = NotNullUtils.notNull((Integer) jsonObject.get("currentPage"));
        Integer locked = NotNullUtils.notNull((Integer) jsonObject.get("locked"));
        String nameInfo = jsonObject.get("nameInfo").toString();


        Page<HUser> page = new Page<>(currPage, 13);
        IPage<HUser> pageData = null;

        if (locked == 0) {
            //查询未冻结用户
            if (NotNullUtils.String(nameInfo)) {
                //根据用户名或昵称查询
                pageData = userService.page(page, new QueryWrapper<HUser>()
                        .eq("locked", 0)
                        .and(wrapper -> wrapper
                                .like("userName", nameInfo)
                                .or()
                                .like("nickName", nameInfo))
                        .orderByAsc("create_time"));

                //将重要信息清除
                List<HUser> userList = pageData.getRecords();
                for (HUser user : userList
                ) {
                    user.setPassword("");
                    user.setSign("");
                    user.setAvatar("");
                }
                pageData.setRecords(userList);

            } else {
                // 查全部
                pageData = userService.page(page, new QueryWrapper<HUser>()
                        .eq("locked", 0)
                        .orderByAsc("create_time"));
                //将重要信息清除
                List<HUser> userList = pageData.getRecords();
                for (HUser user : userList
                ) {
                    user.setPassword("");
                    user.setSign("");
                    user.setAvatar("");
                }
                pageData.setRecords(userList);
            }
        } else {
            //查询冻结用户
            if (NotNullUtils.String(nameInfo)) {
                //根据用户名或昵称查询
                pageData = userService.page(page, new QueryWrapper<HUser>()
                        .eq("locked", 1)
                        .and(wrapper -> wrapper
                                .like("userName", nameInfo)
                                .or()
                                .like("nickName", nameInfo))
                        .orderByAsc("create_time"));

                //将重要信息清除
                List<HUser> userList = pageData.getRecords();
                for (HUser user : userList
                ) {
                    user.setPassword("");
                    user.setSign("");
                    user.setAvatar("");
                }
                pageData.setRecords(userList);
            } else {
                // 查全部
                pageData = userService.page(page, new QueryWrapper<HUser>()
                        .eq("locked", 1)
                        .orderByAsc("create_time"));
                //将重要信息清除
                List<HUser> userList = pageData.getRecords();
                for (HUser user : userList
                ) {
                    user.setPassword("");
                    user.setSign("");
                    user.setAvatar("");
                }
                pageData.setRecords(userList);
            }
        }
        return Result.success(pageData);
    }

    /*
     *冻结/还原账户
     * @return
     */
    @GetMapping("/admin/locked/{id}/{locked}")
    @RequiresAuthentication
    public Result lockedForUser(@PathVariable("id") Integer id, @PathVariable("locked") Integer locked) {

        Boolean updateById = userService.lockedForUser(id, locked);

        if (updateById) {
            return Result.success(null);
        } else {
            return Result.failure("操作失败，请重试");
        }

    }


    /*
     *获取博主头像
     * @return
     */
    @GetMapping("/bloggerAvatar")
    public Result bloggerAvatar() {
        HUser blogger = userService.getById(1);
        ReplyDto replyDto = new ReplyDto();
        replyDto.setAvatar("https://" + serverIp + blogger.getAvatar());
        /*replyDto.setAvatar("https://"+serverIp+ blogger.getAvatar());*/
        replyDto.setId(blogger.getId());
        replyDto.setNickName(blogger.getNickName());
        return Result.success(replyDto);

    }

    /**
     * 退出
     *
     * @return
     */
    @RequiresAuthentication //需要有权限才能退出
    @PostMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }
}

