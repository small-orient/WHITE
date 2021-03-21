package com.baidetu.entity;

import com.baidetu.common.dto.ReplyDto;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 测试
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "HComment对象", description = "")
public class HComment implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "一条评论的id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //评论用户ID
    private Integer userId;

    //回复显示的用户信息
    private ReplyDto user;

    @ApiModelProperty(value = "评论内容")
    private String content;

    /*//回复内容
    private String  replyContent;*/

    //子回复：每条评论可以有多个子评论
    private List<HComment> replyComments = new ArrayList<>();

    //每个子评论只有一个父评论
    private HComment parentComment;

    //父评论用户名
    private String parentName;

    //博客标题
    private String title;

    @ApiModelProperty(value = "评论ID")
    @TableField("parentCommentId")
    private Integer parentCommentId;

    @ApiModelProperty(value = "评论博客ID")
    @TableField("blogId")
    private Integer blogId;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "评论时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//页面写入数据库时格式化
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd hh:mm:ss") //数据库导出页面时json格式化
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
