package com.baidetu.dao;

import com.baidetu.entity.HBlog;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
public interface HBlogMapper extends BaseMapper<HBlog> {

    //条件查询单个博客
    @Override
    HBlog selectOne(@Param(Constants.WRAPPER) Wrapper<HBlog> queryWrapper);

    //根据id查询博客
    @Override
    HBlog selectById(Serializable id);

    //查询集合
    @Override
    List<HBlog> selectList(@Param(Constants.WRAPPER) Wrapper<HBlog> queryWrapper);


}
