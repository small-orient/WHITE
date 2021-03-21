package com.baidetu.util;

import com.baidetu.common.dto.PaginationDto;
import com.baidetu.entity.HBlogKeyword;
import com.baidetu.entity.HKeyword;
import com.baidetu.service.HBlogKeywordService;
import com.baidetu.service.HKeywordService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class KeywordUtils {

    @Autowired
    private HBlogKeywordService blogKeywordService;

    @Autowired
    private HKeywordService keywordService;

    public static KeywordUtils keywordUtils;

    @PostConstruct
    public void init() {
        keywordUtils = this;
        keywordUtils.blogKeywordService = this.blogKeywordService;
        keywordUtils.keywordService = this.keywordService;
    }

    /**
     * 页面关键字下拉列表
     *
     * @param pageData
     * @return
     */
    public static List<PaginationDto> addKeyWord(IPage<PaginationDto> pageData) {

        //遍历博客分页对象，将每篇博客关键字添加进去
        List<PaginationDto> paginationDtoList = pageData.getRecords();
        for (PaginationDto paginationDto : paginationDtoList
        ) {
            List<String> keyword = new ArrayList<>();
            //获取关键字id
            List<HBlogKeyword> blogKeywordList = keywordUtils.blogKeywordService
                    .list(new QueryWrapper<HBlogKeyword>()
                            .eq("bId", paginationDto.getId()));
            for (HBlogKeyword blogKeyword : blogKeywordList
            ) {
                HKeyword hKeyword = keywordUtils.keywordService.getById(blogKeyword.getKId());
                keyword.add(hKeyword.getKeyword());
            }

            //添加关键字字段到博客分页对象内
            paginationDto.setKeyword(keyword);
        }

        return paginationDtoList;
    }

    /**
     * 写博客储存关键字
     *
     * @param blogId      博客id
     * @param keywordList 根据用户输入获取的关键字集合
     * @param sign        是否为更新操作，true是，false否
     * @return
     */
    public static Boolean saveKeyWord(Integer blogId, List<String> keywordList, Boolean sign) {

        if (sign)
            //更新博客标志为true,则根据博客Id删除所有旧的联合主键
            keywordUtils.blogKeywordService
                    .remove(new QueryWrapper<HBlogKeyword>().eq("bId", blogId));


        // 遍历更新的关键字集合
        for (String s : keywordList) {
            HKeyword keyword = keywordUtils.keywordService
                    .getOne(new QueryWrapper<HKeyword>()
                            .eq("keyword", s));
            if (keyword != null) {
                //数据库有，直接获取关键字id
                HBlogKeyword blogKeyword = new HBlogKeyword();
                blogKeyword.setKId(keyword.getId());
                blogKeyword.setBId(blogId);
                //查询博客与关键字中间表，是否已有联合主键，防止更新博客时，更新中间表造成主键冲突
                HBlogKeyword hBlogKeyword = keywordUtils.blogKeywordService.
                        getOne(new QueryWrapper<HBlogKeyword>().
                                eq("bId", blogId).
                                eq("kId", keyword.getId()));
                if (hBlogKeyword == null) {
                    //无值，更新
                    boolean keywordFlag = keywordUtils.blogKeywordService.save(blogKeyword);
                    //储存不成功，返回false
                    if (!keywordFlag)
                        return false;
                }
                //有值，不管

            } else {
                //新关键字，先更新关键字表，再获取关键字id
                boolean keywordSave = keywordUtils.keywordService.save(new HKeyword().setKeyword(s));
                if (keywordSave) {
                    HBlogKeyword blogKeyword = new HBlogKeyword();
                    //获取保存的关键字id
                    blogKeyword.setKId(keywordUtils.keywordService
                            .getOne(new QueryWrapper<HKeyword>()
                                    .eq("keyword", s)).getId()
                    );
                    blogKeyword.setBId(blogId);
                    boolean keywordFlag = keywordUtils.blogKeywordService.save(blogKeyword);
                    //储存不成功，返回false
                    if (!keywordFlag)
                        return false;
                }
            }
        }


        return true;
    }
}
