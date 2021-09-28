package com.jtyjy.gateway.exception;

import com.jtyjy.gateway.web.ResultCode;

public class BizException extends RuntimeException{

    /**
     * 业务状态码
     */
    private Integer code;
    /**
     * 业务信息
     */
    private String message;

    /**
     * 根异常，保持异常链
     */
    private Throwable caused;


    public BizException(ResultCode resultEnum, Throwable caused) {
        super(caused);
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }


    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(ResultCode enumType) {
        super(enumType.getMessage());
        this.code = enumType.getCode();
        this.message = enumType.getMessage();
    }


    public BizException(Integer code, Throwable caused) {
        super(caused);
        this.code = code;
        this.caused = caused;
    }



    public BizException(Integer code, String message, Throwable caused) {
        super(message, caused);
        this.code = code;
        this.message = message;
        this.caused = caused;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Throwable getCaused() {
        return caused;
    }
}
