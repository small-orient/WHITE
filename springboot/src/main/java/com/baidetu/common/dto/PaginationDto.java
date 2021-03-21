package com.baidetu.common.dto;

import com.baidetu.entity.HComment;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 自定义博客分页Dto
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PaginationDto implements Serializable {

    @ApiModelProperty(value = "博客ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "昵称")
    @TableField("nickName")
    private String nickName;

    @ApiModelProperty(value = "博客类型名称")
    @TableField("typeName")
    private String typeName;

    @ApiModelProperty(value = "博客标题")
    private String title;

    @ApiModelProperty(value = "创作标识")
    @TableField("createSign")
    private String createSign;

    @ApiModelProperty(value = "点赞数")
    @TableField("likes")
    private Long likes;

    @ApiModelProperty(value = "关键字")
    private List<String> keyword;

    //评论集合
    private List<HComment> comment;

    @ApiModelProperty(value = "评论数")
    @TableField("commentHit")
    private Integer commentHit;

    @ApiModelProperty(value = "点击数")
    @TableField("clickHit")
    private Integer clickHit;

    @ApiModelProperty(value = "内容")
    @NotBlank(message = "博客内容不能为空")
    @TableField("content")
    private String content;


    @ApiModelProperty(value = "博客封面图")
    @TableField("blogImg")
    private String blogImg;


    @ApiModelProperty(value = "发布状态")
    @TableField("published")
    private Integer published;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "评论开启")
    @TableField("commentOpen")
    private Integer commentOpen;

    @ApiModelProperty(value = "赞赏开启")
    @TableField("appreciation")
    private Integer appreciation;

    @ApiModelProperty(value = "推荐开启")
    @TableField("recommend")
    private Integer recommend;

    @ApiModelProperty(value = "转载状态")
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

    @ApiModelProperty(value = "摘要")
    private String summary;


}
