package com.baidetu.service;

import com.baidetu.common.Result;
import com.baidetu.entity.HUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
public interface HUserService extends IService<HUser> {

    /**
     * 上传头像
     *
     * @param file
     * @return
     */
    Result uploadAvatar(MultipartFile file, Integer userId, HttpServletRequest request);

    /*
     *冻结/还原账户
     * @return
     */
    Boolean lockedForUser(@Param("id") Integer id, @Param("locked") Integer locked);

}
