package com.baidetu.service;

import com.baidetu.common.Result;
import com.baidetu.entity.HBlog;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface HBlogService extends IService<HBlog> {

    /**
     * 上传博客封面图
     *
     * @param file
     * @return
     */
    Result upload(MultipartFile file, String onePath, HttpServletRequest request);

    /**
     * 移除博客封面图
     */
    Result deleteImg(String path, String onePath);

    /**
     * 博客分页展示或草稿展示
     *
     * @param dataJson
     * @param published
     * @param deleted
     * @return
     */
    Result pageOrDraftOrRecycle(String dataJson, Integer published, Integer deleted);
}
