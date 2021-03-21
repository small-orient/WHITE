package com.baidetu;

import com.baidetu.common.dto.PaginationDto;
import com.baidetu.dao.HBlogMapper;
import com.baidetu.dao.HUserMapper;
import com.baidetu.entity.HBlog;
import com.baidetu.entity.HUser;
import com.baidetu.entity.HBlogtype;
import com.baidetu.service.HBlogService;
import com.baidetu.service.HUserService;
import com.baidetu.service.HBlogtypeService;
import com.baidetu.service.PaginationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class BaidetuApplicationTests {

    @Autowired
    HUserMapper bloggerMapper;

    @Resource
    HUserService bloggerService;

    @Resource
    HBlogService blogService;

    @Resource
    HBlogMapper blogMapper;

    @Resource
    PaginationService paginationService;

    @Autowired
    HBlogtypeService blogtypeService;


    @Test
    void contextLoads() {
        HUser blogger = bloggerMapper.selectById(1);
        System.out.println(blogger);

        System.out.println("---------------");

        HBlog blog = blogMapper.selectById(1);
        System.out.println(blog);

        System.out.println("---------------");

        HBlog b = new HBlog();
        b.setTitle("测试插入02");
        int count = blogMapper.insert(b);
        System.out.println(count);
        System.out.println("---------------");
    }

    @Test
    void test() {
        int a = 0;
        a = ++a;
        System.out.println(a);
    }

    //测试查询
    @Test
    void Blogger() {

        System.out.println("-----------------------------");
        HUser hBlogger = bloggerService.getOne(new QueryWrapper<HUser>().eq("id", "1"));
        System.out.println(hBlogger);
        System.out.println("-----------------------------");
       /* HBlog blog = blogService.getOne(new QueryWrapper<HBlog>().eq("title", "测试标题01"));
        System.out.println(blog);*/
    }

    //测试条件分页查询
    @Test
    void blog() {

        Page<PaginationDto> page = new Page<>(1, 9);
        QueryWrapper<PaginationDto> wrapper = new QueryWrapper<>();
        wrapper.eq("typeName", "java");
        IPage<PaginationDto> pageData = paginationService.page(page, wrapper);
        List<PaginationDto> records = pageData.getRecords();
        for (PaginationDto data : records
        ) {
            System.out.println(data);

        }

    }

    //测试模糊查询
    @Test
    void fuzzy() {

        Page<PaginationDto> page = new Page<>(1, 9);
        QueryWrapper<PaginationDto> wrapper = new QueryWrapper<>();
        wrapper.like("blog.title", "模糊");
        IPage<PaginationDto> pageData = paginationService.page(page, wrapper);
        List<PaginationDto> records = pageData.getRecords();
        for (PaginationDto data : records
        ) {
            System.out.println(data);

        }

    }

    //测试博客类别分页
    @Test
    void blogTypePagination() {

        Page<HBlogtype> page = new Page<>(1, 20);
        IPage<HBlogtype> pageData = blogtypeService.page(page, null);
        List<HBlogtype> dataRecords = pageData.getRecords();

       /*
       //没在数据库表中添加每个类别含有的博客数字段，使用下面自定义的getBlogCount方法获取博客数
       Map<String, Integer> map = new HashMap<>();
        Integer blogCount = 0;
        String typeName = null;
        for (HBlogtype blogtype:dataRecords
             ) {
            System.out.println(blogtype.getTypeName());
            typeName = blogtype.getTypeName();
            blogCount = blogtypeService.getBlogCount(typeName,new QueryWrapper<HBlogtype>().eq("typeName",typeName));
            map.put(typeName,blogCount);
        }*/

        System.out.println(dataRecords);
    }


}
