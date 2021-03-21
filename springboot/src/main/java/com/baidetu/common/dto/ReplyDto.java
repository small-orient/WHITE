package com.baidetu.common.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 自定义评论回复用户信息DTO
 */
@Data
public class ReplyDto implements Serializable {

    private Integer id;

    @ApiModelProperty(value = "昵称")
    @NotBlank(message = "昵称不能为空")
    @TableField("nickName")
    private String nickName;

    @ApiModelProperty(value = "个性签名")
    @TableField("sign")
    private String sign;


    @ApiModelProperty(value = "头像地址")
    @TableField("avatar")
    private String avatar;

}

