package com.baidetu.dao;

import com.baidetu.entity.HComment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
public interface HCommentMapper extends BaseMapper<HComment> {

    /**
     * 根据博客id获取评论集合
     *
     * @param
     * @return
     */
    List<HComment> listComment(
            @Param(Constants.WRAPPER) Wrapper<HComment> queryWrapper);

    /**
     * 保存评论信息方法
     *
     * @param comment
     * @return
     */
    Boolean saveComment(HComment comment);

    /**
     * 查询单个评论
     *
     * @param
     * @return
     */
    HComment findOne(@Param(Constants.WRAPPER)
                             Wrapper<HComment> queryWrapper);

    /**
     * 更新评论
     *
     * @param
     * @return
     */
    Boolean updateById(@Param("id") Long id, @Param("parentCommentId") Long parentCommentId);

    /**
     * 删除评论
     *
     * @param
     * @return
     */
    Boolean deleteById(Long id);
}
