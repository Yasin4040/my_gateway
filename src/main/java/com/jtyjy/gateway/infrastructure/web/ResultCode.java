package com.jtyjy.gateway.infrastructure.web;

import java.util.HashMap;
import java.util.Map;

public enum ResultCode {
    OK(200, "ok"),

    BAD_REQUEST(400, "bad request body"),

    UNAUTHORIZED(401, "unauthorized, please try login"),

    PAYMENT_REQUIRED(402, "payment required"),

    FORBIDDEN(403, "permission denied, please try again"),

    NOT_FOUND(404, "record not found"),

    PASSWORD_ERROR(405, "password error"),

    DUPLICATE_ERROR(407, "record duplicate error"),

    INTERNAL_SERVER_ERROR(500, "internal server error"),

    NOT_IMPLEMENTED(501, "not implement"),

    BAD_GATEWAY(502, "bad gateway"),

    SERVICE_UNAVAILABLE(503, " {%s} service is not available"),

    GATEWAY_TIMEOUT(504, "request time out"),

    HTTP_VERSION_NOT_SUPPORT(505, "unsupported http request"),

    ;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    private final static Map<Integer, ResultCode> RESULT_CODE_MAP = new HashMap<>();

    static {
        ResultCode[] values = ResultCode.values();
        for (ResultCode value : values) {
            RESULT_CODE_MAP.put(value.getCode(), value);
        }
    }


    public static ResultCode valueOf(Integer code) {
        return RESULT_CODE_MAP.get(code);
    }

    public boolean containsCode(Integer code) {
        return RESULT_CODE_MAP.containsKey(code);
    }

    /**
     * 状态编码
     */
    private Integer code;
    /**
     * 状态默认信息
     */
    private String message;


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
