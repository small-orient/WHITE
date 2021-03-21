package com.baidetu.controller;


import com.baidetu.common.Result;
import com.baidetu.common.annotation.CommentLimit;
import com.baidetu.entity.HComment;
import com.baidetu.entity.HUser;
import com.baidetu.service.HCommentService;
import com.baidetu.service.HUserService;
import com.baidetu.util.ComHitUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 博客评论
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
@RestController
@RequestMapping("/comment")
public class HCommentController {

    @Resource
    private HCommentService commentService;

    @Resource
    private HUserService userService;


    /**
     * 博客页面显示评论信息
     *
     * @param blogId
     * @return
     */
    @GetMapping("/comments/{blogId}")
    public Result comments(@PathVariable Long blogId) {

        List<HComment> commentList = commentService.listComment(null, blogId.intValue());

        return Result.success(commentList);
    }

    /**
     * 保存博客评论信息
     *
     * @return
     */
    @PostMapping("/save")
    @CommentLimit(seconds = 60, maxCount = 3) //30S内最多允许连续评论3次
    @RequiresAuthentication
    public Result saveBlogComments(@RequestBody HComment comment) {

        Boolean saveComment = commentService.saveComment(comment);

        if (saveComment) {
            //此时该博客评论数+1
            ComHitUtils.commentHitAdd(comment.getBlogId());
            //返回
            return Result.success(null);
        }

        return Result.failure("评论失败，请重试");
    }

    /**
     * 获取每篇博客的评论
     *
     * @param
     * @return
     */
    @GetMapping("/blog/comment")
    public Result getMyBlogComment() {

        List<List<HComment>> all = commentService.findAll();

        return Result.success(all);
    }

    /**
     * 删除评论
     *
     * @param
     * @return
     */
    @GetMapping("/delete/{commentId}")
    public Result deleteComment(@PathVariable Long commentId) {

        //此时该博客评论数-1
        HComment comment = commentService.findOne(new QueryWrapper<HComment>().eq("id", commentId));
        if (comment.getBlogId() != 0)
            ComHitUtils.commentHitLess(comment.getBlogId());
        //删除
        Boolean delete = commentService.deleteById(commentId);
        if (delete)
            return Result.success(null);

        return Result.failure("删除评论失败请重试");
    }

    /**
     * 博主评论
     *
     * @param
     * @return
     */
    @GetMapping("/my")
    public Result myComment() {

        List<HComment> comments = commentService.list(new QueryWrapper<HComment>(), 1);

        return Result.success(comments);
    }

    /**
     * 普通用户评论
     */
    @GetMapping("/userComment/{userId}")
    public Result userComment(@PathVariable("userId") Integer userId) {

        HUser user = userService.getById(userId);
        //防止直接调后台接口
        if (user.getLocked() == 1)
            return Result.failure("用户已被冻结，操作失败");

        List<HComment> comments = commentService.getComments(new QueryWrapper<HComment>(), userId);

        return Result.success(comments);
    }


}

