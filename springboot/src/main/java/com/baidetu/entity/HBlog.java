package com.baidetu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
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
@ApiModel(value = "HBlog对象", description = "")
public class HBlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "博客ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "博客标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "博客封面图")
    @TableField("blogImg")
    private String blogImg;

    @ApiModelProperty(value = "摘要")
    @NotBlank(message = "摘要不能为空")
    private String summary;

    @ApiModelProperty(value = "博客内容")
    @NotBlank(message = "博客内容不能为空")
    private String content;

    @ApiModelProperty(value = "点击数")
    @TableField("clickHit")
    private Integer clickHit;

    @ApiModelProperty(value = "创作标识")
    @TableField("createSign")
    private String createSign;

    @ApiModelProperty(value = "点赞数")
    @TableField("likes")
    private Long likes;

    @ApiModelProperty(value = "评论数")
    @TableField("commentHit")
    private Integer commentHit;

    @ApiModelProperty(value = "博客类型ID")
    @TableField("blogTypeId")
    private Integer blogTypeId;

    @ApiModelProperty(value = "关键字")
    private List<String> keyword;

    //评论集合
    private List<HComment> comment;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "博主ID")
    @TableField("userId")
    private Integer userId;


    @ApiModelProperty(value = "发布状态")
    @TableField("published")
    private Integer published;

    @ApiModelProperty(value = "评论开启")
    @TableField("commentOpen")
    private Integer commentOpen;

    @ApiModelProperty(value = "赞赏开启")
    @TableField("appreciation")
    private Integer appreciation;

    @ApiModelProperty(value = "推荐开启")
    @TableField("recommend")
    private Integer recommend;

    @ApiModelProperty(value = "推荐状态")
    @TableField("shareStatement")
    private Integer shareStatement;

    @ApiModelProperty(value = "发表时间")
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")//页面写入数据库时格式化
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd") //数据库导出页面时json格式化
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")//页面写入数据库时格式化
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd") //数据库导出页面时json格式化
    private Date updateTime;


}
