package com.baidetu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 测试
 * @since 2020-10-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HBlogKeyword对象", description="")
public class HBlogKeyword implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "博客ID")
    @TableId(value = "bId", type = IdType.ID_WORKER)
    private Integer bId;

    @ApiModelProperty(value = "关键字Id")
    @TableField("kId")
    private Integer kId;


}
