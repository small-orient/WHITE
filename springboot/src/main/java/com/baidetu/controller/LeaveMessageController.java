package com.baidetu.controller;

import com.baidetu.common.Result;
import com.baidetu.common.annotation.CommentLimit;
import com.baidetu.entity.HComment;
import com.baidetu.service.HCommentService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定义留言板controller
 */
@RestController
@RequestMapping("/message")
public class LeaveMessageController {

    @Resource
    private HCommentService commentService;

    /**
     * 留言板显示评论信息（默认留言板所属博客id=0）
     *
     * @param
     * @return
     */
    @GetMapping("/leaveMessages")
    public Result getLeaveMessage() {

        List<HComment> commentList = commentService.listComment(null, 0);

        return Result.success(commentList);
    }

    /**
     * 保存留言板评论信息
     *
     * @return
     */
    @PostMapping("/saveLeaveMessage")
    @CommentLimit(seconds = 60, maxCount = 3) //30S内最多允许连续评论3次
    @RequiresAuthentication
    public Result saveLeaveMessage(@RequestBody HComment comment) {

        if ("\n".equals(comment.getContent()))
            //防止前端未输入内容直接回车提交
            return Result.failure("评论不能为空");

        Boolean saveComment = commentService.saveComment(comment);

        if (saveComment)
            return Result.success(null);


        return Result.failure("留言失败，请重试");
    }

}
