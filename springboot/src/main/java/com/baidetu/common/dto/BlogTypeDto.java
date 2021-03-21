package com.baidetu.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BlogTypeDto implements Serializable {

    @ApiModelProperty(value = "博客类型ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String typeName;

    @ApiModelProperty(value = "各类型博客数")
    private Integer blogNumber;

}
