package com.baidetu.controller;

import com.baidetu.common.Result;
import com.baidetu.dao.HBlogMapper;
import com.baidetu.entity.HBlog;
import com.baidetu.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/frontBlog")
public class HBlogFrontController {

    @Resource
    private HBlogService blogService;

    @Resource
    private PaginationService paginationService;

    @Resource
    private HBlogtypeService blogtypeService;

    @Resource
    private HUserService userService;

    @Resource
    private HBlogKeywordService blogKeywordService;

    @Resource
    private HKeywordService keywordService;

    @Resource
    private HBlogMapper blogMapper;

    /**
     * 博客点击数
     *
     * @param blogId
     * @return
     */
    @GetMapping("/addClick/{blogId}")
    public Result addClick(@PathVariable(name = "blogId") Long blogId) {
        HBlog blog = blogService.getById(blogId);
        blog.setClickHit(blog.getClickHit() + 1);

        boolean update = blogService.updateById(blog);

        if (update)
            return Result.success(blog);

        //不是重要的错误不用返回失败方法
        return Result.success(null);

    }

}
