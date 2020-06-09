package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 自定义全局异常处理类
 */
@ControllerAdvice
public class BaseExceptionHandler {

    /**
     * 处理方法
     */
    /*@ExceptionHandler(value = NullPointerException.class)   //该方法只捕获空指针异常
    public Result handlerError(Exception e){

    }

    @ExceptionHandler(value = IndexOutOfBoundsException.class)   //该方法只捕获下标越界异常
    public Result handlerError(Exception e){

    }*/

    @ExceptionHandler(value = Exception.class)   //该方法只捕获所有异常
    @ResponseBody  // 注意：@ControllerAdvice并不包含@ResponseBody
    public Result handlerError(Exception e){
        return new Result(false, StatusCode.ERROR,"执行失败："+e.getMessage());
    }
}
