package com.baidetu.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 自定义用户个人资料回显信息
 */
@Data
public class UserInfo implements Serializable {
    @ApiModelProperty(value = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @TableField("userName")
    private String userName;

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

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不正确")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "冻结")
    @TableField("locked")
    private Integer locked;

}
