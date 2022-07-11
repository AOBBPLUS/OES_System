package com.oes.servicebase.exceptionhandler;

import com.oes.commonutils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * 异常处理类
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //所有异常都能被该函数处理
    @ExceptionHandler(Exception.class)
    @ResponseBody // 返回json数据
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error().message("执行了全局的异常类...");
    }

    //自定义异常处理
    @ExceptionHandler(OesException.class)
    @ResponseBody // 返回json数据
    public Result error(OesException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return Result.error().message(e.getMessage()).code(e.getCode());
    }
}
