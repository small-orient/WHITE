package com.baidetu.common.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 自定义登录DTO
 */
@Data
public class LoginDto implements Serializable {

    @NotBlank(message = "用户名不能为空")
    @TableField("userName")
    private String userName;

    @TableField("password")
    @NotBlank(message = "密码不能为空")
    private String password;

}
