package com.baidetu.dao;

import com.baidetu.common.dto.PaginationDto;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaginationMapper extends BaseMapper<PaginationDto> {

    //重写分页方法，使用多表联查分页
    List<PaginationDto> page(@Param("page") IPage<PaginationDto> page,
                             @Param(Constants.WRAPPER) Wrapper<PaginationDto> queryWrapper);

    //彻底删除博客方法
    int deleteById(Long id);

    //还原
    int revert(Long id);

    //获取自定义博客信息集合
    List<PaginationDto> selectByList(@Param(Constants.WRAPPER) Wrapper<PaginationDto> queryWrapper);

    PaginationDto selectByOne(@Param(Constants.WRAPPER) Wrapper<PaginationDto> queryWrapper);

    //查询单个博客信息
    PaginationDto getById(@Param("blogId") Long blogId);

    //按照年份获取博客集合
    List<PaginationDto> findByYear(String year);

    //获取博客所属年份集合
    List<String> findGroupByYear();
}
