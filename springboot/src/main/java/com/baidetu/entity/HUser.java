package com.baidetu.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value = "HBlogger对象", description = "")
public class HUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @TableField("userName")
    private String userName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    @NotBlank(message = "密码不能为空")
    private String password;

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

    @ApiModelProperty(value = "上一次登陆时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//页面写入数据库时格式化
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd") //数据库导出页面时json格式化
    private Date lastTime;

    @ApiModelProperty(value = "最后一次登陆时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//页面写入数据库时格式化
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd") //数据库导出页面时json格式化
    private Date loginTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd")//页面写入数据库时格式化
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd") //数据库导出页面时json格式化
    private Date createTime;




}
