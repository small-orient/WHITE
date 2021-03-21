package com.baidetu.common.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 自定义编辑博客回显DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Blog {

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

    @NotBlank(message = "创作标识不能为空")
    private String createSign;

    @ApiModelProperty(value = "博客类型名")
    @NotBlank(message = "类型不能为空")
    private String TypeName;

    @ApiModelProperty(value = "关键字")
    private List<String> keyword;

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

    @ApiModelProperty(value = "转载状态")
    @TableField("shareStatement")
    private Integer shareStatement;

    @ApiModelProperty(value = "博主ID")
    @TableField("userId")
    private Integer userId;


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
