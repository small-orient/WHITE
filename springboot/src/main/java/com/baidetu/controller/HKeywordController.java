package com.baidetu.controller;


import com.baidetu.common.Result;
import com.baidetu.common.dto.PaginationDto;
import com.baidetu.dao.HBlogMapper;
import com.baidetu.entity.HBlogKeyword;
import com.baidetu.entity.HKeyword;
import com.baidetu.service.HBlogKeywordService;
import com.baidetu.service.HKeywordService;
import com.baidetu.service.PaginationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  关键字
 * </p>
 *
 * @author 测试
 * @since 2020-10-25
 */
@RestController
@RequestMapping("/keyword")
public class HKeywordController {

    @Resource
    HKeywordService keywordService;

    @Resource
    HBlogKeywordService blogKeywordService;

    @Resource
    HBlogMapper blogMapper;

    @Resource
    PaginationService paginationService;

    /**
     * 获取关键字
     *
     * @return
     */
    @GetMapping("/get")
    public Result getKeyWord() {
        List<HKeyword> list = keywordService.list();
        assert list != null;
        return Result.success(list);
    }

    /**
     * 根据关键词id查询博客列表
     *
     * @param keywordId
     * @return
     */
    @GetMapping("/getBlogs/{keywordId}")
    public Result getBlogs(@PathVariable(name = "keywordId") Integer keywordId) {

        int count = 0; //记录该标签博客数
        PaginationDto blog = null; //博客对象
        List<PaginationDto> blogList = new ArrayList<>(); //最终页面显示博客集合

        if (keywordId > 0) {
            List<HBlogKeyword> blogKeywordList = blogKeywordService
                    .list(new QueryWrapper<HBlogKeyword>()
                            .eq("kId", keywordId));

            for (HBlogKeyword b : blogKeywordList
            ) {
                List<String> keywords = new ArrayList<>(); //每篇博客的关键字集合
                //根据博客id获取博客
                blog = paginationService
                        .selectByOne(new QueryWrapper<PaginationDto>()
                                .eq("blog.id", b.getBId())
                                .eq("deleted",0)
                                .eq("published",0));

                //防止查不到相应的博客，造成空指针
                if(blog==null)
                    continue;

                //重新获取关键字
                List<HBlogKeyword> keywordList = blogKeywordService.list(new QueryWrapper<HBlogKeyword>()
                        .eq("bId", b.getBId()));
                for (HBlogKeyword k : keywordList
                ) {
                    //添加
                    keywords.add(keywordService.getById(k.getKId()).getKeyword());
                }

                blog.setKeyword(keywords);
                blogList.add(blog);
                count++;
            }

            //获取分页对象
            Page<PaginationDto> page = new Page<>(1, 10);
            //设置显示分页博客集合
            page.setRecords(blogList);
            //设置该标签含有的博客数
            page.setTotal(count);

            return Result.success(page);
        }


        return Result.failure("查询失败，请重试！");
    }

    @PostMapping("/getBlogs/keyword")
    public Result getBlogs(@RequestBody String keyword) {

        int count = 0; //记录该标签博客数
        PaginationDto blog = null; //博客对象
        List<PaginationDto> blogList = new ArrayList<>(); //最终页面显示博客集合

        if (keyword != null) {
            HKeyword hKeyword = keywordService.getOne(new QueryWrapper<HKeyword>()
                    .eq("keyword", keyword));

            List<String> keywords = new ArrayList<>(); //每篇博客的关键字集合
            //获取关键字对应的博客id
            List<HBlogKeyword> blogKeywords = blogKeywordService.list(new QueryWrapper<HBlogKeyword>()
                    .eq("kId", hKeyword.getId()));
            for (HBlogKeyword blogKeyWord : blogKeywords
            ) {

                //根据博客id获取博客
                blog = paginationService
                        .selectByOne(new QueryWrapper<PaginationDto>()
                                .eq("blog.id", blogKeyWord.getBId()));

                //防止查不到相应的博客，造成空指针
                if(blog==null)
                    continue;

                //重新获取关键字
                List<HBlogKeyword> keywordList = blogKeywordService.list(new QueryWrapper<HBlogKeyword>()
                        .eq("bId", blogKeyWord.getBId()));
                for (HBlogKeyword k : keywordList
                ) {
                    //添加
                    keywords.add(keywordService.getById(k.getKId()).getKeyword());
                }
                blog.setKeyword(keywords);
                blogList.add(blog);
                count++;
            }

            //获取分页对象
            Page<PaginationDto> page = new Page<>(1, 10);
            //设置显示分页博客集合
            page.setRecords(blogList);
            //设置该标签含有的博客数
            page.setTotal(count);

            return Result.success(page);
        }else {
            return Result.failure(null);
        }

    }



}

