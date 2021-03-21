package com.baidetu.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baidetu.common.Result;
import com.baidetu.common.dto.Blog;
import com.baidetu.common.dto.PaginationDto;
import com.baidetu.dao.HBlogMapper;
import com.baidetu.entity.*;
import com.baidetu.service.*;
import com.baidetu.util.KeywordUtils;
import com.baidetu.util.NotNullUtils;
import com.baidetu.util.ShiroUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * <p>
 * 博客设置
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/blog")
public class HBlogController {

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

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 博客分页
     *
     * @param
     * @return
     */
    @PostMapping("/pagination")
    public Result list(@RequestBody String dataJson) {
        return blogService.pageOrDraftOrRecycle(dataJson, 0, 0);

    }

    /**
     * 草稿分页（修改发布状态搜索为1即可即可）
     *
     * @param
     * @return
     */
    @PostMapping("/draft")
    public Result draftList(@RequestBody String dataJson) {
        return blogService.pageOrDraftOrRecycle(dataJson, 1, 0);

    }

    /**
     * 回收站分页（修改逻辑删除值为1即可即可）
     *
     * @param
     * @return
     */
    @PostMapping("/recycle")
    public Result recycleList(@RequestBody String dataJson) {
        return blogService.pageOrDraftOrRecycle(dataJson, 0, 1);
    }

    /**
     * 获取热文(点击数高为准)
     *
     * @return
     */
    @GetMapping("/hot")
    public Result getHot() {
        //使用redis缓存，使用hash类型，造成乱序是我们不想看到的，所以最后改进为使用list
        //如果仍要使用hash，可以在最后根据点击数排序即可

        //返回的List<HBlog>集合
        List<HBlog> returnList = new ArrayList<>();
        //从redis获取热点数据
        /* Map<Object, Object> hotBlog = redisTemplate.opsForHash().entries("hotBlog");*/
        List<Object> hotBlog = redisTemplate.opsForList().range("hotBlog", 0, -1);
        assert hotBlog != null;
        if (hotBlog.isEmpty()) {
            List<HBlog> blogList = blogMapper
                    .selectList(new QueryWrapper<HBlog>()
                            .eq("deleted", 0)
                            .eq("published", 0)
                            .orderByDesc("blog.clickHit"));

            //获取clickHit、title数据、id
            for (HBlog blog : blogList
            ) {
                //存为对象保存
                HBlog hBlog = new HBlog();
                hBlog.setId(blog.getId());
                hBlog.setTitle(blog.getTitle());
                hBlog.setClickHit(blog.getClickHit());
                //保存至最终要返回前端的集合中
                returnList.add(hBlog);
            }
            //将map存入redis
            redisTemplate.opsForList().leftPush("hotBlog", returnList);
        } else {
            //hotBlog中的元素是一个returnList数组，所以先获取returnList
            List o = (List) hotBlog.get(0);
            for (Object blog : o
            ) {
                HBlog h = (HBlog) blog;
                returnList.add(h);
            }
        }

/* 未使用redis的方法：

    List<HBlog> returnList = blogMapper
                    .selectList(new QueryWrapper<HBlog>()
                            .eq("deleted",0)
                            .eq("published",0)
                            .orderByDesc("blog.clickHit"));*/


        return Result.success(returnList);
    }

    /**
     * 博客detail(回显)
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {

        Long blogId = Long.valueOf(NotNullUtils.notNull(id.intValue()));

        HBlog hblog = blogMapper.selectById(blogId);

        Assert.notNull(hblog, "博客不存在！");

        //查询类型名
        Integer blogTypeId = hblog.getBlogTypeId();
        HBlogtype blogtype = blogtypeService.getById(blogTypeId);
        String typeName = blogtype.getTypeName();
        //查询博主名称
        HUser user = userService.getById(hblog.getUserId());

        //查询关键字
        List<String> keywordList = new ArrayList<>();
        List<HBlogKeyword> blogKeywordList = blogKeywordService
                .list(new QueryWrapper<HBlogKeyword>().eq("bId", hblog.getId()));
        for (HBlogKeyword blogKeyword : blogKeywordList) {
            HKeyword keyword = keywordService.getById(blogKeyword.getKId());
            keywordList.add(keyword.getKeyword());
        }
        /* System.out.println(keywordList+"------------关键字集合回显！");*/
        //保存至自定义PaginationDto信息类,后台编辑回显博客
        PaginationDto blog = new PaginationDto();
        blog.setTypeName(typeName);
        blog.setNickName(user.getNickName());
        blog.setKeyword(keywordList);

        //忽略keyword字段，因为hblog实体类中keyword为null
        BeanUtil.copyProperties(hblog, blog, "keyword");


        return Result.success(blog);
    }

    /**
     * 博客detail(点赞数回显)
     *
     * @param blogId
     * @return
     */
    @GetMapping("/detail/likes/{blogId}")
    public Result likes(@PathVariable(name = "blogId") Long blogId) {
        /*Jedis jedis = new Jedis();*/
        /*String like = jedis.get(Long.toString(blogId));*/
        String like = (String) redisTemplate.opsForValue().get(Long.toString(blogId));
        if (like == null || like.equals("0")) {
            //查询数据库
            HBlog blog = blogService.getById(blogId);
            /*jedis.close();*/
            return Result.success(blog.getLikes());
        } else {
            //有值,则加上数据库的值返回
            HBlog blog = blogService.getById(blogId);
            long data = blog.getLikes() + Long.parseLong(like);
            System.out.println(blog.getLikes() + "----------数据库点赞数");
            System.out.println(Long.parseLong(like) + "----------redis点赞数");
            /*jedis.close();*/
            return Result.success(data);
        }
    }

    /**
     * 上传博客封面图
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @RequiresAuthentication
    public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return blogService.upload(file, "/image/blogImg/", request);
    }

    /**
     * 移除上传博客封面图
     *
     * @param
     * @return
     */
    @PostMapping("/deleteBlogImg")
    @RequiresAuthentication
    public Result deleteBlogImg(@RequestBody String path) {

        String decode = "";
        try {
            decode = URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //这样解码在末尾会多一个“=”号，需去除
        String substring = decode.substring(0, decode.lastIndexOf("=") - 1);
        return blogService.deleteImg(substring, "/image/blogImg/");
    }

    /**
     * 编辑
     *
     * @param blog
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

        //校验是否为博主操作
        Assert.isTrue(ShiroUtil.getProfile().getId() == 1, "无权限编辑！");

        System.out.println(blog.getBlogImg());

        boolean SaveOrUpdateState = false; //博客储存更新标识
        boolean SaveOrUpdateBlogType = false; //博客类别储存更新标识
        //将关键字拿出另存
        List<String> keywordList = blog.getKeyword();
        /*  System.out.println("获取的关键字集合！----------"+keywordList+"------------获取的关键字集合！");*/
        //将关键字字段去除再更新或保存
        blog.setKeyword(null);

        HBlog verifyBlog = null;
        if (blog.getId() != null) {
            //不为空，确认是否有这篇博客，有则为编辑（修改）
            verifyBlog = blogService.getById(blog.getId());
            Assert.notNull(verifyBlog, "博客不存在！");
            //确认是否为令牌登录用户所写博客
            Assert.isTrue(
                    Integer.valueOf(verifyBlog.getUserId()).equals(Integer.valueOf(ShiroUtil.getProfile().getId())), "无权限编辑！");

            //根据博类型名查询类型ID，保存
            String typeName = blog.getTypeName();
            HBlogtype type = blogtypeService
                    .getOne(new QueryWrapper<HBlogtype>().eq("typeName", typeName));
            if (type != null) {
                //数据库有该类型，直接获取类型id保存
                Long typeId = type.getId();
                verifyBlog.setBlogTypeId(typeId.intValue());

               /* //并且该类型博客数+1
                Integer blogNumber = type.getBlogNumber();
                type.setBlogNumber(blogNumber + 1);
                blogtypeService.updateById(type);

                //ps:这里不需要了，因为查询博客类型时会同时遍历博客获取博客数量。
                */

            } else {
                //没有该类型，更新类型表，获取id，保存
                HBlogtype blogtype = new HBlogtype();
                blogtype.setTypeName(typeName);
                SaveOrUpdateBlogType = blogtypeService.save(blogtype);
                //再次查询，将类型id保存
                if (SaveOrUpdateBlogType) {
                    type = blogtypeService
                            .getOne(new QueryWrapper<HBlogtype>().eq("typeName", typeName));
                    verifyBlog.setBlogTypeId(type.getId().intValue());
                }
            }

            //若该博客图片与返回的博客图片不同，则删除旧博客图片
            if (!blog.getBlogImg().equals(verifyBlog.getBlogImg())) {
                Result result = blogService.deleteImg(verifyBlog.getBlogImg(), "/image/blogImg/");
                System.out.println(result + "--------编辑博客修改博客封面图，删除旧图");
            }
            System.out.println(blog.getBlogImg() + "--------前端修改的博客封面图");
            System.out.println(verifyBlog.getBlogImg() + "--------旧图");

            //复制对象blog数据至verifyBlog对象中，typeName字段verifyBlog中没有，不用排除
            BeanUtil.copyProperties(blog, verifyBlog);

            //确认则更新
            SaveOrUpdateState = blogService
                    .update(verifyBlog, new QueryWrapper<HBlog>().eq("id", blog.getId()));

            if (SaveOrUpdateState) {
                //更新博客成功，则更新关键字
                //根据修改的博客id告诉关键字工具类为更新操作，更新关键字，保存
                Boolean saveKeyWord = KeywordUtils
                        .saveKeyWord(verifyBlog.getId().intValue(), keywordList, true);
                if (saveKeyWord) {
                    //更新成功，返回成功
                    //redis删除缓存
                    deleteRedisKey("hotBlog");
                    return Result.success(null);
                }
            } else {
                return Result.failure("操作失败！");
            }

        } else {
            //为空，则为写博客（新增）
            verifyBlog = new HBlog();
            verifyBlog.setUserId(ShiroUtil.getProfile().getId());

            /* System.out.println(blog.getKeyword()+"---------------keyword-------------");*/
            String typeName = blog.getTypeName();
            HBlogtype type = blogtypeService
                    .getOne(new QueryWrapper<HBlogtype>()
                            .eq("typeName", typeName));
            Long typeId = 0L;

            if (type != null) {
                //数据库有该类型
                typeId = type.getId();
                verifyBlog.setBlogTypeId(typeId.intValue());
                //复制对象blog数据至verifyBlog对象中，忽略UserId字段,typeName字段verifyBlog中没有，不用排除
                BeanUtil.copyProperties(blog, verifyBlog, "userId");
                //储存博客
                SaveOrUpdateState = blogService.save(verifyBlog);

                if (SaveOrUpdateState) {

                    //1)存储博客后，获取博客生成的ID，然后存储该博客关键字
                    HBlog echoBlog = blogMapper
                            .selectOne(new QueryWrapper<HBlog>()
                                    .eq("title", verifyBlog.getTitle())
                                    .eq("deleted", 0));
                    Boolean saveKeyWord = KeywordUtils
                            .saveKeyWord(echoBlog.getId().intValue(), keywordList, false);

                    if (saveKeyWord) {
                        //redis删除缓存
                        deleteRedisKey("hotBlog");
                        return Result.success(null);
                    }
                }
            } else {
                //没有，保存新博客类型
                HBlogtype blogtype = new HBlogtype();
                blogtype.setTypeName(typeName);
                SaveOrUpdateBlogType = blogtypeService.save(blogtype);

                //再次查询，将类型id保存
                if (SaveOrUpdateBlogType) {
                    type = blogtypeService
                            .getOne(new QueryWrapper<HBlogtype>()
                                    .eq("typeName", typeName));
                    typeId = type.getId();
                    verifyBlog.setBlogTypeId(typeId.intValue());
                    //复制对象blog数据至verifyBlog对象中，忽略UserId字段,typeName字段verifyBlog中没有，不用排除
                    BeanUtil.copyProperties(blog, verifyBlog, "userId");
                    //储存
                    SaveOrUpdateState = blogService.save(verifyBlog);
                }

                if (SaveOrUpdateState) {
                    //1)存储博客后，获取博客生成的ID，然后存储该博客关键字
                    HBlog echoBlog = blogMapper
                            .selectOne(new QueryWrapper<HBlog>()
                                    .eq("title", verifyBlog.getTitle())
                                    .eq("deleted", 0));
                    Boolean saveKeyWord = KeywordUtils
                            .saveKeyWord(echoBlog.getId().intValue(), keywordList, false);

                    if (saveKeyWord) {
/*                       Jedis jedis = new Jedis();
                        //缓存中同时删除数据
                        jedis.del("paginationDtoList");
                        jedis.close();*/

                        //redis删除缓存
                        deleteRedisKey("hotBlog");
                        return Result.success(null);
                    }

                }
            }

        }
        return Result.failure("操作失败！");
    }

    /**
     * 博客点赞+1
     *
     * @param blogId
     * @return
     */
    @GetMapping("/addLike/{blogId}")
    public Result addLike(@PathVariable Long blogId) {
        return paginationService.addLikeForRedis(blogId);
    }

    /**
     * 博客点赞-1
     *
     * @param blogId
     * @return
     */
    @GetMapping("/removeLike/{blogId}")
    public Result removeLike(@PathVariable Long blogId) {
        return paginationService.removeLikeForRedis(blogId);
    }

    /**
     * 删除博客
     *
     * @param
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/delete")
    public Result delete(@Validated @RequestBody Object[] ids) {

        //校验是否为博主操作
        Assert.isTrue(ShiroUtil.getProfile().getId() == 1, "无权限操作！");

        boolean removeFlag = false; //删除博客标识
        JSONArray idsArray = JSONUtil.parseArray(ids); //博客id的json数组

        for (int i = 0; i < idsArray.size(); i++) {
            JSONObject idsArrayJSONObject = idsArray.getJSONObject(i);
            /*System.out.println(idsArrayJSONObject.getStr("id"));*/
            //删除博客
            removeFlag = blogService
                    .removeById(Integer.parseInt(idsArrayJSONObject.getStr("id")));

            if (!removeFlag) {
                //失败，抛异常
                return Result.failure("------删除id为：" + Integer.parseInt(idsArrayJSONObject.getStr("id")) + "的博客异常-------");
            }

            //redis删除缓存
            deleteRedisKey("hotBlog");
        }

        return Result.success(null);


    }

    /**
     * 草稿箱还原
     *
     * @param
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/revert/{id}")
    public Result revert(@PathVariable(name = "id") Long id) {

        Long blogId = Long.valueOf(NotNullUtils.notNull(id.intValue()));


        Boolean revert = paginationService.revert(blogId);
        if (!revert)
            return Result.failure("还原博客编号为：" + blogId + " 的博客异常，请重试！");

        //redis删除缓存
        deleteRedisKey("hotBlog");

        return Result.success(null);
    }

    /**
     * 回收站彻底删除
     *
     * @param
     * @return
     */
    @RequiresAuthentication
    @PostMapping("/recycleDelete")
    public Result recycleDelete(@Validated @RequestBody Object[] ids) {

        //校验是否为博主操作
        Assert.isTrue(ShiroUtil.getProfile().getId() == 1, "无权限操作！");

        boolean removeFlag; //删除博客标识
        JSONArray idsArray = JSONUtil.parseArray(ids); //博客id的json数组

        for (int i = 0; i < idsArray.size(); i++) {
            JSONObject idsArrayJSONObject = idsArray.getJSONObject(i);
            /*System.out.println(idsArrayJSONObject.getStr("id"));*/

            //彻底删除前，删除该博客关键字关联表记录
            blogKeywordService
                    .remove(new QueryWrapper<HBlogKeyword>()
                            .eq("bId",
                                    Integer.parseInt(idsArrayJSONObject.getStr("id"))));
            //彻底删除博客
            removeFlag = paginationService
                    .deleteById(Long.parseLong(idsArrayJSONObject.getStr("id")));

            if (!removeFlag) {
                //失败，抛异常
                return Result.failure("------删除id为：" + Integer.parseInt(idsArrayJSONObject.getStr("id")) + "的博客异常-------");
            }

        }

        //redis删除缓存
        deleteRedisKey("hotBlog");

        return Result.success(null);
    }

    /**
     * 删除redis保存的博客数据
     *
     * @param key
     */
    public void deleteRedisKey(String key) {
        redisTemplate.delete(key);
    }
}

