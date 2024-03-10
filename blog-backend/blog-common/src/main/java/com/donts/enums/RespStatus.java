package com.donts.enums;

import com.donts.response.StatusCode;

public enum RespStatus implements StatusCode {

    SUCCESS(200, "ok"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_ERROR(500, "服务器内部错误"),
    ;

    private final int code;
    private final String message;

    RespStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }
}