package com.baidetu.service;

import com.baidetu.common.Result;
import com.baidetu.common.dto.PaginationDto;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PaginationService extends IService<PaginationDto> {

    //彻底删除博客
    Boolean deleteById(Long id);

    //还原博客
    Boolean revert(Long id);

    //获取自定义博客信息集合
    List<PaginationDto> selectByList(@Param(Constants.WRAPPER) Wrapper<PaginationDto> queryWrapper);

    PaginationDto selectByOne(@Param(Constants.WRAPPER) Wrapper<PaginationDto> queryWrapper);

    //查询单个博客信息
    PaginationDto getById(@Param("blogId") Long blogId);

    //按年份获取博客集合
    public Map<String, List<PaginationDto>> findByYear();

    //将点赞数+1
    Result addLikeForRedis(@Param("blogId") Long blogId);

    //将点赞数-1
    Result removeLikeForRedis(@Param("blogId") Long blogId);

    //定时存储redis点赞数，完成数据同步
    void saveLike();

    //分页重写
    public IPage<PaginationDto> page(IPage<PaginationDto> page, Wrapper<PaginationDto> queryWrapper);
}
