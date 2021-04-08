package com.baidetu.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baidetu.common.Result;
import com.baidetu.common.dto.PaginationDto;
import com.baidetu.dao.HBlogMapper;
import com.baidetu.entity.HBlog;
import com.baidetu.service.HBlogService;
import com.baidetu.service.PaginationService;
import com.baidetu.util.KeywordUtils;
import com.baidetu.util.NotNullUtils;
import com.baidetu.util.QuickSortUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
@Service
public class HBlogServiceImpl extends ServiceImpl<HBlogMapper, HBlog>
        implements HBlogService {

    @Resource
    private PaginationService paginationService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private QuickSortUtils quickSortUtils;

    /**
     * 上传博客封面图
     *
     * @param file
     * @param onePath //存储路径
     * @return
     */
    @Override
    public Result upload(MultipartFile file, String onePath, HttpServletRequest request) {
        //判断不为空
        if (file.isEmpty()) {
            return Result.failure("文件为空！");
        }
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //存于服务器文件名
        String newFilename = System.currentTimeMillis() + "." +
                originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        //获取文件大小
        /*long fileSize = file.getSize();*/

        //文件类型
        /*  String fileContentType = file.getContentType();*/

        //存储路径
        String path = null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath() + "static" + onePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String url = request.getContextPath() + onePath;
        assert path != null;
        //根据此路径创建文件
        File filePath = new File(path);
        System.out.println("path文件的保存路径：" + path);
        System.out.println("url文件的相对路径：" + url);
        System.out.println("path文件的绝对路径：" + filePath.getAbsolutePath());
        //判断存储路径是否存在
        if (!filePath.exists() && !filePath.isDirectory()) {
            System.out.println("目录不存在，创建目录:" + filePath);
            filePath.mkdirs(); // mkdir()不会创建目录,找不到相应路径时返回false;而mkdirs()当目录不存在时则会创建相应目录
        }

        //在指定路径下创建一个文件
        File targetFile = new File(filePath.getAbsolutePath(), newFilename);

        try {
            //拷贝上传文件至targetFile
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure(400, originalFilename + ":上传失败", null);
        }

        //返回相对路径名
        return Result.success(onePath + newFilename);
    }

    /**
     * 移除博客封面图
     *
     * @param path    图片存储相对路径（包含图片时间戳名称，如/image/blogImg/65465.png）
     * @param onePath 图片存储相对路径（仅为图片存储文件夹，如/image/blogImg/）
     * @return
     */
    @Override
    public Result deleteImg(String path, String onePath) {
        //截取成储存的时间戳格式
        String substring = path.substring(path.lastIndexOf("/") + 1, path.length());

        //绝对路径
        String realPath = null;
        try {
            realPath = ResourceUtils.getURL("classpath:").getPath() + "static" + onePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert realPath != null;
        File filePath = new File(realPath);

        //拼接成最终路径
        File img = new File(filePath.getAbsolutePath() + "/" + substring);
        System.out.println(img.getPath() + "--------删除路径");
        System.out.println(img.getAbsolutePath() + "--------img.getAbsolutePath()绝对路径");
        //删除
        boolean delete = img.delete();
        if (delete) {
            System.out.println("删除图片成功------------");
            return Result.success(null);
        }

        return Result.failure("删除失败");
    }


    /**
     * 博客分页展示、草稿展示、回收站展示
     *
     * @param dataJson
     * @param published
     * @return
     */

    @Override
    public Result pageOrDraftOrRecycle(String dataJson, Integer published, Integer deleted) {

        JSONObject jsonObject = JSONUtil.parseObj(dataJson);

        Integer currentPage = (Integer) jsonObject.get("currentPage");
        String typeName = (String) jsonObject.get("typeName");
        String title = jsonObject.get("title").toString();


/*System.out.println("--------------currentPage:"+currentPage);
        System.out.println("--------------typeName:"+typeName);
        System.out.println("--------------title:"+title);*/

        String decodeTitle = null; //解析标题编码标识
        try {
            //有值并且不为空则转码
            if (NotNullUtils.String(title))
                decodeTitle = URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // System.out.println(decodeTitle+"+++++++++++--------");

        Integer currPage = NotNullUtils.notNull(currentPage);

        Page<PaginationDto> page = new Page<>(currPage, 13);
        IPage<PaginationDto> pageData = new Page<>();
        if (NotNullUtils.String(typeName)) {
            //有typeName值带参查询
            if (deleted == 0) {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("typeName", typeName)
                        .eq("deleted", 0)
                        .eq("published", published)
                        .orderByDesc("blog.create_time"));

            } else {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("typeName", typeName)
                        .eq("deleted", 1)
                        .orderByDesc("blog.create_time"));
            }
            //获取关键字
            List<PaginationDto> paginationDtoList = KeywordUtils.addKeyWord(pageData);
            //重新设置博客分页对象
            pageData.setRecords(paginationDtoList);
        } else if (NotNullUtils.String(decodeTitle)) {
            //有title值带参查询
            if (deleted == 0) {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("deleted", 0)
                        .eq("published", published)
                        .and(wrapper -> {
                            try {
                                return wrapper
                                        .like("blog.summary", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blog.title", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blog.content", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blogtype.typeName", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            return wrapper;
                        })
                        .orderByDesc("blog.create_time"));
            } else {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("deleted", 1)
                        .and(wrapper -> {
                            try {
                                return wrapper
                                        .like("blog.summary", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blog.title", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blog.content", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blogtype.typeName", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            return wrapper;
                        })
                        .orderByDesc("blog.create_time"));
            }
            //获取关键字
            List<PaginationDto> paginationDtoList = KeywordUtils.addKeyWord(pageData);
            //重新设置博客分页对象
            pageData.setRecords(paginationDtoList);

        } else if (!"".equals(decodeTitle) && !"".equals(typeName) && "null".equals(decodeTitle) && !"null".equals(typeName)) {
            if (deleted == 0) {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("typeName", typeName)
                        .eq("blog.summary", decodeTitle)
                        .eq("deleted", 0)
                        .eq("published", published)
                        .orderByDesc("blog.create_time"));
            } else {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("typeName", typeName)
                        .eq("blog.summary", decodeTitle)
                        .eq("deleted", 1)
                        .orderByDesc("blog.create_time"));

            }
            //获取关键字
            List<PaginationDto> paginationDtoList = KeywordUtils.addKeyWord(pageData);
            //重新设置博客分页对象
            pageData.setRecords(paginationDtoList);
        } else {
            // 查全部
            if (deleted == 0) {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("deleted", 0)
                        .eq("published", published)
                        .orderByDesc("blog.create_time"));

            } else {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("deleted", 1)
                        .orderByDesc("blog.create_time"));

            }
            //获取关键字
            List<PaginationDto> paginationDtoList = KeywordUtils.addKeyWord(pageData);
            //重新设置博客分页对象
            pageData.setRecords(paginationDtoList);

        }
        return Result.success(pageData);
    }




   /*
   这里是使用redis缓存分页的方法。

   使用redis的hash类型会造成排序混乱，并且与mybatisplus分页对象IPage一起使用时，需要分解IPage.

   排序混乱可以通过自定义的快速排序工具类解决。

   不使用的原因：
               1.分页方法耦合性太严重，多个地方的分页为了公用一个方法，导致使用redis缓存的时候粒度不好把控，
               例如博客类型页面，点击某类型会出现相应的博客类型的博客，但如果统一被缓存了，那么就不能显示对应的博客类型（不过下面的代码我优化了已经可以了）。

               2.使用其他接口失效。我在让回收站还原博客时，会显示还原成功，但在redis并未刷新（看不到还原的博客），
                 我在blogController对应的还原操作已经删除了key。并且去除缓存后，又可以显示还原博客，那么就是还原操作并没有成功删除该key。
                 这个出错原因已经找到，但这里捣鼓了太久了，所以我仅恢复了热门博客接口的正常的使用，仅保留热门博客存储到redis中。

                            2021-03-20:发现redis删不掉key，是用错了方法。
                                       例如redisTemplate.opsForList().remove(key,0,value)，删除list集合中第一个等于value的值，在hotBlog接口使用时失效，删不掉，hash估计也同理；
                                       换成redisTemplate.delete(key)即可 .

               3.前端不能翻页。翻页失效，这就很头疼了，所以必须暂时的舍弃。
                    解决：将页数也同时缓存，非本页数据需要重新查询数据库。
               综上暂且不用。


   */

/*   @Override
    public Result pageOrDraftOrRecycle(String dataJson, Integer published, Integer deleted) {

        JSONObject jsonObject = JSONUtil.parseObj(dataJson);

        Integer currentPage = (Integer) jsonObject.get("currentPage");
        String typeName = (String) jsonObject.get("typeName");
        String title = jsonObject.get("title").toString();

        String decodeTitle = null; //解析标题编码标识
            try {
            //有值并且不为空则转码
            if (NotNullUtils.String(title))
                decodeTitle = URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Integer currPage = NotNullUtils.notNull(currentPage);

        boolean hashMark = false;
        Page<PaginationDto> page = new Page<>(currPage, 13);
        IPage<PaginationDto> pageData = new Page<>();
        if (NotNullUtils.String(typeName)) {
            //有typeName值带参查询
            if (deleted == 0) {
                    pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                            .eq("typeName", typeName)
                            .eq("deleted", 0)
                            .eq("published", published)
                            .orderByDesc("blog.create_time"));
            } else {
                    pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                            .eq("typeName", typeName)
                            .eq("deleted", 1)
                            .orderByDesc("blog.create_time"));

            }
        } else if (NotNullUtils.String(decodeTitle)) {
            //有title值带参查询
            if (deleted == 0) {
                pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                        .eq("deleted", 0)
                        .eq("published", published)
                        .and(wrapper -> {
                            try {
                                return wrapper
                                        .like("blog.summary", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blog.title", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blog.content", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                        .or()
                                        .like("blogtype.typeName", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            return wrapper;
                        })
                        .orderByDesc("blog.create_time"));
            } else {
                    pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                            .eq("deleted", 1)
                            .and(wrapper -> {
                                try {
                                    return wrapper
                                            .like("blog.summary", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                            .or()
                                            .like("blog.title", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                            .or()
                                            .like("blog.content", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"))
                                            .or()
                                            .like("blogtype.typeName", URLDecoder.decode(URLDecoder.decode(title, "utf-8"), "utf-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                return wrapper;
                            })
                            .orderByDesc("blog.create_time"));
            }

        } else if (!"".equals(decodeTitle) && !"".equals(typeName) && "null".equals(decodeTitle) && !"null".equals(typeName)) {
            if (deleted == 0) {
                    pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                            .eq("typeName", typeName)
                            .eq("blog.summary", decodeTitle)
                            .eq("deleted", 0)
                            .eq("published", published)
                            .orderByDesc("blog.create_time"));
            } else {
                    pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                            .eq("typeName", typeName)
                            .eq("blog.summary", decodeTitle)
                            .eq("deleted", 1)
                            .orderByDesc("blog.create_time"));
            }
        } else {
            // 查全部
            if (deleted == 0) {
                //redis查询
                Map<Object, Object> all_deleted_0 = redisTemplate.opsForHash().entries("all_deleted_0");
                System.out.println("all_deleted_0:"+all_deleted_0.toString()+"-----------------------");
                if (all_deleted_0.isEmpty()) {
                    pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                            .eq("deleted", 0)
                            .eq("published", published)
                            .orderByDesc("blog.create_time"));

                    saveByRedis(pageData,"all_deleted_0");
                }else {
                    pageData = getByRedis(pageData,all_deleted_0);
                    hashMark = true;
                }
            } else {
                //redis查询
                Map<Object, Object> all_deleted_1 = redisTemplate.opsForHash().entries("all_deleted_1");
                System.out.println("all_deleted_1:"+all_deleted_1.toString()+"-----------------------");
                if (all_deleted_1.isEmpty()) {
                    pageData = paginationService.page(page, new QueryWrapper<PaginationDto>()
                            .eq("deleted", 1)
                            .orderByDesc("blog.create_time"));
                    saveByRedis(pageData,"all_deleted_1");
                }else {
                    pageData = getByRedis(pageData,all_deleted_1);
                    hashMark = true;
                }
            }
        }

        //获取关键字
        List<PaginationDto> paginationDtoList = KeywordUtils.addKeyWord(pageData);
        //hash存储会出现乱序，那么需要重新排序
        if (hashMark){
            paginationDtoList = quickSortUtils.sort(paginationDtoList);
        }
        //重新设置博客分页对象
        pageData.setRecords(paginationDtoList);

        return Result.success(pageData);
    }

   //博客分页数据存入redis方法
    public void saveByRedis(IPage<PaginationDto> pageData, String redisHash){
        //保存的key-value集合
        Map<Object, Object> result = new HashMap<>();

        List<PaginationDto> paginationDtoList = pageData.getRecords();
        for (PaginationDto paginationDto:paginationDtoList
        ) {
            //key为博客id，value为PaginationDto对象
            result.put(paginationDto.getId().toString(),paginationDto);
        }
        //将博客对象存入redis
        redisTemplate.opsForHash().putAll(redisHash,result);
        //将当前pageData对象的部分属性保存到redis，方便获取时重新给这个对象赋值
        Map<String, Long> pageDataParam = new HashMap<>();
        pageDataParam.put("Current",pageData.getCurrent());
        pageDataParam.put("Pages",pageData.getPages());
        pageDataParam.put("Size",pageData.getSize());
        pageDataParam.put("Total",pageData.getTotal());
        redisTemplate.opsForHash().putAll("pageData",pageDataParam);
    }

    //从redis获取博客分页数据方法
    public IPage<PaginationDto> getByRedis(IPage<PaginationDto> pageData,Map<Object, Object> value){
        //重新封装博客数据进行分页的list集合
        List<PaginationDto> returnList = new ArrayList<>();
        //遍历集合
        Set<Object> keySet = value.keySet();
        System.out.println(keySet.toString()+"+++++++++++++++++++++");
        for (Object key:keySet
        ) {
            System.out.println(key.toString()+"-------------------------------");
            PaginationDto o = (PaginationDto) value.get(key);
            //重新封装回List<PaginationDto>
            returnList.add(o);
        }
        //获取redis中的pageData相关参数
        pageData.setCurrent(Long.parseLong(Objects.requireNonNull(redisTemplate.opsForHash().get("pageData", "Current")).toString()));
        pageData.setPages(Long.parseLong(Objects.requireNonNull(redisTemplate.opsForHash().get("pageData","Pages")).toString()));
        pageData.setSize(Long.parseLong(Objects.requireNonNull(redisTemplate.opsForHash().get("pageData","Size")).toString()));
        pageData.setTotal(Long.parseLong(Objects.requireNonNull(redisTemplate.opsForHash().get("pageData","Total")).toString()));
        //保存博客数据到分页对象中
        pageData.setRecords(returnList);
        //返回
        return pageData;
    }*/


}
