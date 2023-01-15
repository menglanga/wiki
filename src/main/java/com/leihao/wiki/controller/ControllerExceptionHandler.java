package com.leihao.wiki.controller;

import com.leihao.wiki.exception.BusinessException;
import com.leihao.wiki.response.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOG= LoggerFactory.getLogger(ControllerExceptionHandler.class);


    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResponse validExceptionHandler(BindException e){
        CommonResponse response=new CommonResponse();
        LOG.warn("参数校验失败：{}",e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        response.setSuccess(false);
        response.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return response;

    }


    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResponse businessExceptionHandler(BusinessException e){
        CommonResponse response=new CommonResponse();
        LOG.warn("业务异常：{}",e.getCode().getDesc());
        response.setSuccess(false);
        response.setMessage(e.getCode().getDesc());
        return response;

    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResponse exceptionHandler(Exception e){
        CommonResponse response=new CommonResponse();
        LOG.error("系统异常：",e);
        response.setSuccess(false);
        response.setMessage("系统异常，请联系管理员！");
        return response;

    }

}
