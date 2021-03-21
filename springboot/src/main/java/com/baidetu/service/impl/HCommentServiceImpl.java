package com.baidetu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baidetu.common.dto.ReplyDto;
import com.baidetu.dao.HBlogMapper;
import com.baidetu.dao.HCommentMapper;
import com.baidetu.dao.HUserMapper;
import com.baidetu.entity.HBlog;
import com.baidetu.entity.HComment;
import com.baidetu.entity.HUser;
import com.baidetu.service.HCommentService;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class HCommentServiceImpl extends ServiceImpl<HCommentMapper, HComment>
        implements HCommentService {

    @Resource
    private HCommentMapper commentMapper;

    @Resource
    private HUserMapper userMapper;

    @Resource
    HBlogMapper blogMapper;

    //注入服务器IP
    @Value("${server.ip}")
    String serverIp;


    /**
     * 获取评论集合
     * 一级为绝对父级
     * 二级为该绝对父级下所有子代集合
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public List<HComment> listComment(Wrapper<HComment> queryWrapper, Integer blogId) {
        //获取顶级（绝对父级）评论
        List<HComment> commentList = commentMapper.listComment(
                new QueryWrapper<HComment>()
                        .eq("parentCommentId", -1)
                        .eq("blogId", blogId)
                        .orderByDesc("create_time"));
        //存储顶级评论
        return eachComment(commentList);
    }

    /**
     * 获取评论人
     *
     * @param id
     * @return
     */
    private ReplyDto getCommentUser(Integer id) {

        //查出每个评论对应用户
        HUser user = userMapper.selectById(id);
        ReplyDto replyDto = new ReplyDto();
        BeanUtil.copyProperties(user, replyDto);

        //设置头像访问路径
        String userAvatar = replyDto.getAvatar();
        replyDto.setAvatar("https://" + serverIp + userAvatar);
        /*  replyDto.setAvatar("https://"+serverIp + userAvatar);*/

        return replyDto;
    }

    /**
     * 复制顶级父评论集合至新集合
     *
     * @param commentList
     * @return 获得新顶级父评论集合
     */
    private List<HComment> eachComment(List<HComment> commentList) {
        List<HComment> comments = new ArrayList<>();
        for (HComment hComment : commentList
        ) {
            //添加评论人信息
            ReplyDto replyDto = getCommentUser(hComment.getUserId());
            hComment.setUser(replyDto);
            //复制到新集合
            comments.add(hComment);
        }

        //合并顶级父评论下的所有子代评论到同一集合
        combineChildren(comments);

        //返回
        return comments;
    }

    /**
     * 临时存放子代评论集合
     */
    private List<HComment> temReplys = new ArrayList<>();

    /**
     * 处理每个顶级父评论，将其子代所有放入同一集合
     *
     * @param commentList
     */
    private void combineChildren(List<HComment> commentList) {

        for (HComment hComment : commentList
        ) {
            //子评论集合
            List<HComment> replyComments = getChild(hComment.getId());

            //遍历
            for (HComment reply : replyComments
            ) {
                //添加该评论的评论人信息
                ReplyDto replyDto = getCommentUser(reply.getUserId());
                reply.setUser(replyDto);
                //储存父级评论用户名称
                reply.setParentName(hComment.getUser().getNickName());
                //循环迭代，找出子代,并在方法中存放所有子代至临时集合temReplys
                recursively(reply);
            }

            //重新设置子代集合
            hComment.setReplyComments(temReplys);
            //清空临时集合
            temReplys = new ArrayList<>();

        }

    }

    /**
     * 迭代
     *
     * @param hComment
     */
    private void recursively(HComment hComment) {
        //先存放第一个子评论
        temReplys.add(hComment);
        //获取其子评论集合
        List<HComment> replyComments = getChild(hComment.getId());
        if (replyComments.size() > 0) {
            //继续遍历
            for (HComment reply : replyComments
            ) {
                //添加该评论的评论人信息
                ReplyDto replyDto = getCommentUser(reply.getUserId());
                reply.setUser(replyDto);
                //储存父级评论用户名称
                reply.setParentName(hComment.getUser().getNickName());

                if (getChild(reply.getId()).size() > 0) {
                    //继续迭代
                    recursively(reply);
                } else {
                    //添加第一个子评论的下一级子代评论至临时集合
                    temReplys.add(reply);
                }
            }
        }

    }

    /**
     * 获取一条评论的下一级子代集合
     *
     * @param id
     */
    private List<HComment> getChild(Long id) {
        return commentMapper.listComment(
                new QueryWrapper<HComment>()
                        .eq("parentCommentId", id)
                        .orderByAsc("create_time"));

    }

    @Transactional
    @Override
    public Boolean saveComment(HComment comment) {
        return commentMapper.saveComment(comment);
    }

    @Override
    public HComment findOne(Wrapper<HComment> queryWrapper) {
        return commentMapper.findOne(queryWrapper);
    }

    /**
     * 查询所有博客下的评论
     *
     * @return
     */
    public List<List<HComment>> findAll() {
        //查询非留言的所有评论
        List<HBlog> blogList = blogMapper.selectList(new QueryWrapper<HBlog>()
                .orderByDesc("create_time"));
        //储存评论
        List<List<HComment>> comments = new ArrayList<>();
        for (HBlog blog : blogList
        ) {
            List<HComment> commentList = commentMapper.listComment(new QueryWrapper<HComment>()
                    .eq("blogId", blog.getId())
                    .orderByDesc("create_time"));
            for (HComment comment : commentList
            ) {
                ReplyDto user = getCommentUser(comment.getUserId());
                comment.setUser(user);
                comment.setTitle(blog.getTitle());
            }
            //储存
            comments.add(commentList);
        }
        return comments;
    }

    @Transactional
    @Override
    public Boolean updateById(Long id, Long parentCommentId) {
        return commentMapper.updateById(id, parentCommentId);
    }

    @Transactional
    @Override
    public Boolean deleteById(Long id) {
        HComment comment = commentMapper.findOne(
                new QueryWrapper<HComment>().eq("id", id));
        //获取其父评论id
        Integer parentCommentId = comment.getParentCommentId();
        //查询其第一代所有子评论
        List<HComment> commentList = commentMapper.listComment(
                new QueryWrapper<HComment>().eq("parentCommentId", id));

        for (HComment hComment : commentList
        ) {
            //更新评论父级数据
            Boolean update = commentMapper.updateById(hComment.getId(), parentCommentId.longValue());
            if (!update)
                return false;
        }
        return commentMapper.deleteById(id);
    }

    /**
     * 获取博主评论
     *
     * @param queryWrapper
     * @param userId
     * @return
     */
    @Override
    public List<HComment> list(Wrapper<HComment> queryWrapper, Integer userId) {
        List<HComment> comments = commentMapper.listComment(new QueryWrapper<HComment>()
                .eq("userId", userId).
                        ne("blogId", 0)
                .orderByDesc("create_time"));
        for (HComment comment : comments
        ) {
            if (comment.getBlogId() != 0) {
                HBlog blog = blogMapper.selectById(comment.getBlogId());
                comment.setTitle(blog.getTitle());
                if (comment.getParentCommentId() != -1) {
                    //获取其父评论用户
                    HComment parentComment = commentMapper.findOne(new QueryWrapper<HComment>()
                            .eq("id", comment.getParentCommentId()));
                    ReplyDto user = getCommentUser(parentComment.getUserId());
                    comment.setUser(user);
                }
            }
        }
        return comments;
    }

    /**
     * 获取用户评论，且该用户每条评论作为父级评论储存
     *
     * @param
     * @return
     */
    public List<HComment> getComments(Wrapper<HComment> queryWrapper, Integer userId) {

        //获取用户评论
        List<HComment> commentList = commentMapper.listComment(
                new QueryWrapper<HComment>()
                        .eq("userId", userId)
                        .ne("blogId", 0)
                        .orderByDesc("create_time"));

        for (HComment comment : commentList
        ) {
            //获取用户在该评论下的子评论
            List<HComment> childComments = commentMapper.listComment(new QueryWrapper<HComment>()
                    .eq("parentCommentId", comment.getId())
                    .ne("blogId", 0)
                    .orderByDesc("create_time"));
            for (HComment hComment : childComments
            ) {
                //设置子评论用户信息
                ReplyDto commentUser = getCommentUser(hComment.getUserId());

                hComment.setUser(commentUser);
            }
            //保存至用户评论
            comment.setReplyComments(childComments);
            //设置评论用户信息
            ReplyDto commentUser = getCommentUser(comment.getUserId());
            comment.setUser(commentUser);
            //设置博客标题
            HBlog blog = blogMapper.selectById(comment.getBlogId());
            comment.setTitle(blog.getTitle());

        }


        return commentList;
    }


}
