package com.mybatis.mybaties.handler;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mybatis.mybaties.utils.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    private ResultUtil exceptionHandler(HttpServletRequest request, Exception e) {
        ResultUtil resultUtil = new ResultUtil();
        resultUtil.setCode(501);
        resultUtil.setData(false);
        resultUtil.setErrMsg(e.getMessage());
        resultUtil.setMsg("系统异常");
        return resultUtil;
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseBody
    private ResultUtil tokenExpiredExceptionHandler(HttpServletRequest request, TokenExpiredException e) {
        ResultUtil resultUtil = new ResultUtil();
        resultUtil.setCode(303);
        resultUtil.setData(false);
        resultUtil.setErrMsg(e.getMessage());
        resultUtil.setMsg("token失效");
        return resultUtil;
    }

}
