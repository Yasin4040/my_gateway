package com.jtyjy.gateway.exception;

import com.jtyjy.gateway.web.Result;
import com.jtyjy.gateway.web.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常拦截处理类,处理系统出现的各种异常，并统一封装返回提示
 */
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    /**
     * 处理业务异常
     *
     * @param e   BizException
     * @return ResponseEntity
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public <T> Result<T> bizErrorHandler(BizException e) {
        logger.error(e.toString(), e);
        return Result.failure(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public <T> Result<T> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        logger.error(e.toString(), e);
        StringBuilder sb = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(objectError -> {
            sb.append(objectError.getDefaultMessage()).append(";");
        });
        return Result.failure(400, sb.toString());
    }

    /**
     * 处理默认的系统异常，即没有封装自定义异常的情况下就到该方法处理
     *
     * @param e   Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public <T> Result<T> defaultErrorHandler(Exception e) {
        logger.error(e.toString(), e);
        return Result.failure(ResultCode.INTERNAL_SERVER_ERROR);
    }


}
