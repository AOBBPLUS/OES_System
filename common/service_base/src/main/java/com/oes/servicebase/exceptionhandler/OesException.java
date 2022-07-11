package com.oes.servicebase.exceptionhandler;

import com.oes.commonutils.ResultCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

// 抛出异常处理类
@EqualsAndHashCode(callSuper = true)
@Data
public class OesException extends RuntimeException {

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "提示消息")
    private String message;

    /*
     * 接收枚举类型对象
     */
    public OesException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.message = resultCodeEnum.getMessage();
    }

    public OesException() {
    }

    public OesException(String message) {
        this.message = message;
    }
}
