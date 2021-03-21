package com.baidetu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 友链
 * @since 2020-09-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "友链", description = "")
public class HLink implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "友情链接ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "链接地址")
    @TableField("linkUrl")
    private String linkUrl;

    @ApiModelProperty(value = "链接名称")
    @TableField("linkName")
    private String linkName;

    @ApiModelProperty(value = "图片地址")
    @TableField("linkImg")
    private String linkImg;


    @ApiModelProperty(value = "添加链接时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新链接时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
