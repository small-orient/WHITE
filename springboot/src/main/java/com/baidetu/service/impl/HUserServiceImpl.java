package com.baidetu.service.impl;

import com.baidetu.common.Result;
import com.baidetu.dao.HUserMapper;
import com.baidetu.entity.HUser;
import com.baidetu.service.HBlogService;
import com.baidetu.service.HUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
@Service
public class HUserServiceImpl extends ServiceImpl<HUserMapper, HUser> implements HUserService {

    @Resource
    private HBlogService blogService;

    @Resource
    private HUserMapper userMapper;

    //注入服务器IP
    @Value("${server.ip}")
    String serverIp;


    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    @Override
    public Result uploadAvatar(MultipartFile file, Integer userId, HttpServletRequest request) {

        //调用博客上传封面图的方法（复用），上传头像
        Result upload = blogService.upload(file, "/image/headImg/", request);
        //将相对路径保存至数据库
        HUser user = new HUser();
        user.setId(userId);
        user.setAvatar(upload.getData().toString());
        //更新前删除用户头像图片
        HUser hUser = userMapper.selectById(userId);
        System.out.println(hUser.getAvatar() + "----------用户修改前头像------------");
        if (hUser.getAvatar() != null) {
            Result result = blogService.deleteImg(hUser.getAvatar(), "/image/headImg/");
            System.out.println(result + "--------------删除信息-----------------");
        }
        //更新
        int updateAvatar = userMapper.updateById(user);
        if (updateAvatar > 0) {
            /*return Result.success("https://"+serverIp+ user.getAvatar());*/
            return Result.success("https://" + serverIp + user.getAvatar());
        }
        return Result.failure("修改失败");
    }

    /*
     *冻结/还原账户
     * @return
     */
    @Transactional
    @Override
    public Boolean lockedForUser(Integer id, Integer locked) {

        HUser user = new HUser();
        user.setId(id);
        user.setLocked(locked);
        int update = userMapper.updateById(user);
        return update > 0;
    }
}
