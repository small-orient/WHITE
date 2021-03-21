package com.baidetu.controller;


import cn.hutool.json.JSONUtil;
import com.baidetu.common.Result;
import com.baidetu.entity.HLink;
import com.baidetu.service.HLinkService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 友情链接controller
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/link")
public class HLinkController {

    @Resource
    private HLinkService linkService;

    /**
     * 获取友情链接
     *
     * @return
     */
    @GetMapping("/list")
    public Result getLink() {
        Jedis jedis = new Jedis();
        String list = jedis.get("linkList");
        List<HLink> linkList = null;
        if (list != null) {
            //redis有
            linkList = JSONUtil.toList(JSONUtil.parseArray(list), HLink.class);
        } else {
            //redis没有，查数据库
            linkList = linkService.list();
            //存入redis
            String jsonStr = JSONUtil.toJsonStr(linkList);
            jedis.set("linkList", jsonStr);
        }
        jedis.close();
        return Result.success(linkList);
    }

    /**
     * 添加友链
     *
     * @return
     */
    @PostMapping("/add")
    @RequiresAuthentication
    public Result add(@RequestBody HLink link) {
        if (link != null) {
            linkService.save(link);
            Jedis jedis = new Jedis();
            //缓存中同时删除数据
            jedis.del("linkList");
            jedis.close();
            return Result.success(null);
        } else {
            return Result.failure("添加失败,请确认填写信息无误");
        }
    }

    /**
     * 删除友链
     *
     * @return
     */
    @GetMapping("/delete/{id}")
    @RequiresAuthentication
    public Result delete(@PathVariable Long id) {
        boolean remove = linkService.removeById(id);
        if (remove) {
            Jedis jedis = new Jedis();
            //缓存中同时删除数据
            jedis.del("linkList");
            jedis.close();
            return Result.success(null);
        } else {
            return Result.failure("删除失败，请重试");
        }

    }

}

