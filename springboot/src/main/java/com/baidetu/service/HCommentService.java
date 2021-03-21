package com.baidetu.service;

import com.baidetu.entity.HComment;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
public interface HCommentService extends IService<HComment> {

    /**
     * 根据博客id获取评论集合
     *
     * @param
     * @return
     */
    List<HComment> listComment(
            @Param(Constants.WRAPPER) Wrapper<HComment> queryWrapper,
            @Param("blogId") Integer blogId);


    /**
     * 保存评论信息方法
     *
     * @param comment
     * @return
     */
    Boolean saveComment(HComment comment);

    /*
    获取用户评论
     */
    List<HComment> list(Wrapper<HComment> queryWrapper, Integer userId);

    /**
     * 获取用户评论，且该用户每条评论作为父级评论储存
     *
     * @param
     * @return
     */
    List<HComment> getComments(Wrapper<HComment> queryWrapper, Integer userId);

    /**
     * 查询单个评论
     *
     * @param
     * @return
     */
    HComment findOne(@Param(Constants.WRAPPER)
                             Wrapper<HComment> queryWrapper);

    /**
     * 查询所有评论
     *
     * @return
     */
    List<List<HComment>> findAll();

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
