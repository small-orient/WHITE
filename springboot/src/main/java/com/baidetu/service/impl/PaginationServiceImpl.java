package com.baidetu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baidetu.common.Result;
import com.baidetu.common.dto.PaginationDto;
import com.baidetu.dao.HBlogMapper;
import com.baidetu.dao.PaginationMapper;
import com.baidetu.entity.HBlog;
import com.baidetu.service.PaginationService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PaginationServiceImpl
        extends
        ServiceImpl<PaginationMapper, PaginationDto>
        implements PaginationService {

    @Resource
    PaginationMapper paginationMapper;

    @Resource
    HBlogMapper blogMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    //自定义分页
    @Override
    public IPage<PaginationDto> page(IPage<PaginationDto> page, Wrapper<PaginationDto> queryWrapper) {
        //redis无值，查询数据库，存入redis
        List<PaginationDto> paginationDtoList = this.baseMapper.page(page, queryWrapper);
        return page.setRecords(paginationDtoList);

    }

    //自定义彻底删除博客
    @Transactional
    @Override
    public Boolean deleteById(Long id) {

        boolean flag = false;
        int count = paginationMapper.deleteById(id);
        if (count > 0) {
            flag = true;
        }
        return flag;
    }

    //还原逻辑删除字段
    @Transactional
    @Override
    public Boolean revert(Long id) {
        boolean flag = false;
        int count = paginationMapper.revert(id);
        if (count > 0) {
            flag = true;
        }
        return flag;
    }

    //自定义查询博客集合：转换Markdown成HTML
    @Override
    public List<PaginationDto> selectByList(Wrapper<PaginationDto> queryWrapper) {
        return paginationMapper.selectByList(queryWrapper);
    }

    //查询单个博客
    @Override
    public PaginationDto selectByOne(Wrapper<PaginationDto> queryWrapper) {
        return paginationMapper.selectByOne(queryWrapper);
    }

    //根据博客id查询：详情页显示博客内容
    @Override
    public PaginationDto getById(Long blogId) {

        PaginationDto blog = paginationMapper.getById(blogId);
        Assert.notNull(blog, "该博客不存在！");
        PaginationDto b = new PaginationDto();
        BeanUtil.copyProperties(blog, b);
      /*  //处理博客内容
       String content = b.getContent();
       b.setContent(MarkdownUtils.markdownToHtml(content));*/
        return b;
    }

    //按年份获取博客集合
    @Override
    public Map<String, List<PaginationDto>> findByYear() {
        Map<String, List<PaginationDto>> map = new HashMap<>();
        List<String> years = paginationMapper.findGroupByYear();
        for (String year : years
        ) {
            if (year != null)
                //构建成年份=博客集合的一个map
                map.put(year, paginationMapper.findByYear(year));
        }
        return map;
    }
    /*点赞存在并发问题：
        1.针对redis本身：有A、B两个人，基础点赞数X=1。A进行点赞，此时X=2;
        B也准备点赞，此时B进入addLikeForRedis方法，获得点赞数X=2，但此时，A反悔取消
        点赞，则真实的点赞数X=1，可是B还在addLikeForRedis方法内，并且按照X=2的点赞数+1，
        结果B执行完后，X=3.这就是并发，理论X=2.

        2.likeSet不存在并发安全问题,因为likeSet只存储博客id，点赞++或--两个操作都没有移除博客id，
        只要博客id一添加进入likeSet，那么就是永久性的。当然，后期likeSet存储的id过多，那么需要换成redis存储博客id

        3.点赞数++，--都是存在线程安全问题的操作，可以通过CAS改善
        */

    //储存点赞key
    private final static Set<String> likeSet = new HashSet<>();

    //将点赞数从redis+1
    @Override
    public Result addLikeForRedis(Long blogId) {
        /* Jedis jedis = new Jedis();
        //查询redis
        String likeByBlog = jedis.get(Long.toString(blogId));*/
        Object o = redisTemplate.opsForValue().get(Long.toString(blogId));
        String likeByBlog = null;
        if (o!=null){
            likeByBlog = Objects.requireNonNull(o).toString();
        }
        if (likeByBlog != null && !likeByBlog.equals("0")) {
            //有，则+1
            long like = Long.parseLong(likeByBlog) + 1;
           /* jedis.set(Long.toString(blogId), Long.toString(like));*/
            redisTemplate.opsForValue().set(Long.toString(blogId), Long.toString(like));
        } else {
            //无，设置值，+1
            if (likeByBlog == null) {
                likeByBlog = "1";
            } else {
                likeByBlog = Long.toString(Long.parseLong(likeByBlog) + 1);
            }
            /*jedis.set(Long.toString(blogId), likeByBlog);*/
            redisTemplate.opsForValue().set(Long.toString(blogId), likeByBlog);
        }
        likeSet.add(Long.toString(blogId));
        System.out.println("------------博客点赞数+1记录redis--------");
        /*jedis.close();*/
        return Result.success(null);
    }

    //将点赞数从redis-1
    @Override
    public Result removeLikeForRedis(Long blogId) {
       /* Jedis jedis = new Jedis();
        //查询redis
        String likeByBlog = jedis.get(Long.toString(blogId));*/
        String likeByBlog = Objects.requireNonNull(redisTemplate.opsForValue().get(Long.toString(blogId))).toString();
        if (likeByBlog != null && !likeByBlog.equals("0")) {
            //有，则-1
            long like = Long.parseLong(likeByBlog) - 1;
            /*jedis.set(Long.toString(blogId), Long.toString(like));*/
            redisTemplate.opsForValue().set(Long.toString(blogId), Long.toString(like));
        } else {
            //无，设置值，-1
            if (likeByBlog == null) {
                likeByBlog = "0";
            } else {
                likeByBlog = Long.toString(Long.parseLong(likeByBlog) - 1);
            }

            /*jedis.set(Long.toString(blogId), likeByBlog);*/
            redisTemplate.opsForValue().set(Long.toString(blogId), likeByBlog);
        }

        likeSet.add(Long.toString(blogId));

        System.out.println("------------博客点赞数-1记录redis--------");
        /*jedis.close();*/
        return Result.success(null);
    }

    //定时存储redis点赞数，完成数据同步
    @Transactional
    @Override
    public void saveLike() {
        /*Jedis jedis = new Jedis();*/
        //遍历储存key的集合
        for (String blogId : likeSet) {
            /*String like = jedis.get(blogId);*/
            String like = Objects.requireNonNull(redisTemplate.opsForValue().get(blogId)).toString();
            System.out.println("--储存方法：此时redis储存点赞数为:" + like);
            if (like != null && !like.equals("0")) {
                //redis存有该博客的点赞数，那么进行更新
                HBlog blog = blogMapper.selectById(Long.parseLong(blogId));
                Long likes = blog.getLikes();
                likes = likes + Long.parseLong(like);
                blog.setLikes(likes);
                //同步更新
                blogMapper.updateById(blog);
                //清除redis值
                /*jedis.set(blogId, "0");*/
                redisTemplate.opsForValue().set(blogId, "0");
                System.out.println("---储存方法：清除redis缓存点赞数" + like + "-->" + redisTemplate.opsForValue().get(blogId));
                /*System.out.println("---储存方法：清除redis缓存点赞数" + like + "-->" + jedis.get(blogId));*/
            }
            //为空就不更新
        }
        /*jedis.close();*/
    }


}
