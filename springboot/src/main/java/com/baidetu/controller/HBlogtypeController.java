package com.baidetu.controller;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baidetu.common.Result;
import com.baidetu.dao.HBlogMapper;
import com.baidetu.entity.HBlog;
import com.baidetu.entity.HBlogKeyword;
import com.baidetu.entity.HBlogtype;
import com.baidetu.service.HBlogKeywordService;
import com.baidetu.service.HBlogService;
import com.baidetu.service.HBlogtypeService;
import com.baidetu.service.PaginationService;
import com.baidetu.util.NotNullUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/type")
public class HBlogtypeController {

    @Resource
    HBlogtypeService blogtypeService;

    @Resource
    HBlogService blogService;

    @Resource
    HBlogMapper blogMapper;

    @Resource
    private HBlogKeywordService blogKeywordService;

    @Resource
    private PaginationService paginationService;

    /**
     * 博客类型分页：无需验证用户
     *
     * @param currentPage
     * @return
     */
    @GetMapping("/pagination")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        return blogTypePagination(currentPage);
    }


    /**
     * 博客类型分页：需验证用户
     *
     * @param currentPage
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/pagination")
    public Result listAuthentication(@RequestParam(defaultValue = "1") Integer currentPage) {
        return blogTypePagination(currentPage);
    }

    //博客类型分页方法
    public Result blogTypePagination(Integer currentPage) {

        Integer currPage = NotNullUtils.notNull(currentPage);

        Page<HBlogtype> page = new Page<>(currPage, 9);
        IPage<HBlogtype> pageData = blogtypeService.page(page, null);

        //根据类别计算博客数量
        List<HBlogtype> blogtypeList = pageData.getRecords();

        for (HBlogtype blogType : blogtypeList
        ) {
            int blogNumber = 0; //记录各类别博客数
            List<HBlog> blogList = blogMapper.selectList(new QueryWrapper<HBlog>()
                    .eq("blogTypeId", blogType.getId())
                    .eq("deleted", 0));
            for (HBlog blog : blogList
            ) {
                blogNumber++;
            }
            //保存至实体类
            blogType.setBlogNumber(blogNumber);
            //更新至数据库
            blogtypeService.updateById(blogType);
        }

        //重新封装含博客数量集合
        pageData.setRecords(blogtypeList);

        return Result.success(pageData);
    }


    /**
     * 获取类型名
     *
     * @return
     */
    @GetMapping("/typeName")
    public Result typeNameDeduplication() {

        List<HBlogtype> blogtypeList = blogtypeService.list(new QueryWrapper<>(new HBlogtype()));

        return Result.success(blogtypeList);
    }


    /**
     * 编辑
     *
     * @param
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blogType/delete")
    public Result delete(@Validated @RequestBody Object[] ids) {


        boolean removeFlag = false; //删除博客类别标识

        JSONArray idsArray = JSONUtil.parseArray(ids); //博客类别id的json数组

        for (int i = 0; i < idsArray.size(); i++) {
            JSONObject idsArrayJSONObject = idsArray.getJSONObject(i);

            //查询该类别下所含博客
            List<HBlog> blogList = blogMapper.selectList(new QueryWrapper<HBlog>().eq("blogTypeId", Integer.parseInt(idsArrayJSONObject.getStr("id"))));

            try {
                //删除该类别前删除类别下所含博客,此操作为彻底删除博客，需谨慎！
                for (HBlog hBlog : blogList) {

                    //彻底删除前，删除该博客关键字关联表记录
                    blogKeywordService
                            .remove(new QueryWrapper<HBlogKeyword>()
                                    .eq("bId", hBlog.getId().intValue()));
                    //彻底删除博客
                    paginationService.deleteById(hBlog.getId());

                }

                //删除该类别
                removeFlag = blogtypeService.removeById(Integer.parseInt(idsArrayJSONObject.getStr("id")));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.out.println("删除" +
                        blogtypeService.
                                getOne(new QueryWrapper<HBlogtype>().eq("id", Integer.parseInt(idsArrayJSONObject.getStr("id"))))
                                .getTypeName() + "类型博客异常");
            }


            if (removeFlag)
                return Result.success(null);

        }
        return Result.failure("删除失败");

    }


}

