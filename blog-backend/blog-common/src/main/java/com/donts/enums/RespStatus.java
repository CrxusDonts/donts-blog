package com.donts.enums;

import com.donts.response.StatusCode;

public enum RespStatus implements StatusCode {

    SUCCESS(0, "ok"),
    FAIL(1, "fail"),
    BAD_REQUEST(400, "Bad Request-请求参数错误"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    NO_LOGIN(40001, "用户未登录"),

    AUTHORIZED(40300, "没有操作权限"),

    USERNAME_EXIST(52001, "用户名已存在"),

    USERNAME_NOT_EXIST(52002, "用户名不存在"),

    ARTICLE_ACCESS_FAIL(52003, "文章密码认证未通过"),

    ARTICLE_DETAIL_FAIL(52004, "获取文章详情失败"),

    QQ_LOGIN_ERROR(53001, "qq登录错误");;

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