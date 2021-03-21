package com.baidetu.util;

import com.baidetu.entity.HBlog;
import com.baidetu.service.HBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 操作博客评论数工具类
 */
@Component
public class ComHitUtils {

    @Autowired
    private HBlogService blogService;

    public static ComHitUtils comHitUtils;

    @PostConstruct
    public void init() {
        comHitUtils = this;
        comHitUtils.blogService = this.blogService;
    }

    /**
     * 评论数+1
     */
    public static void commentHitAdd(Integer blogId) {
        HBlog blog = comHitUtils.blogService.getById(blogId);
        blog.setCommentHit(blog.getCommentHit() + 1);
        comHitUtils.blogService.updateById(blog);
    }

    /**
     * 评论数-1
     */
    public static void commentHitLess(Integer blogId) {
        HBlog blog = comHitUtils.blogService.getById(blogId);
        blog.setCommentHit(blog.getCommentHit() - 1);
        comHitUtils.blogService.updateById(blog);
    }

}
