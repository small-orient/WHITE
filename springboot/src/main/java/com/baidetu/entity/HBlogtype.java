package com.baidetu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

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
@ApiModel(value = "HBlogtype对象", description = "")
public class HBlogtype implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "博客类型ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "博客类型名称")
    @TableField("typeName")
    @NotBlank(message = "类型不能为空")
    private String typeName;

    @ApiModelProperty(value = "各类型博客数")
    @TableField("blogNumber")
    private Integer blogNumber;


}
