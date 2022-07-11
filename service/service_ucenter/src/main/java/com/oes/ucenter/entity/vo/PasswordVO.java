package com.oes.ucenter.entity.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "修改密码VO", description = "学生端修改密码的VO")
public class PasswordVO {
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码,发送过来的密码就应该为MD5加密后的密码")
    private String password;

    @ApiModelProperty(value = "验证码")
    private String code;
}