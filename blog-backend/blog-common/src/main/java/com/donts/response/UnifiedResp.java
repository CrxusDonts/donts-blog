package com.donts.response;

import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.io.Serializable;

import static com.donts.enums.RespStatus.INTERNAL_ERROR;
import static com.donts.enums.RespStatus.SUCCESS;

@Data
public final class UnifiedResp<T extends Serializable> implements Serializable {

    private StatusCode code;
    private String message;
    private T data;

    //无参构造方法中将响应码置为DefaultStatus中的SUCCESS
    public UnifiedResp() {
        this.setCode(SUCCESS);
        this.message = SUCCESS.message();
    }

    public UnifiedResp(T data) {
        this();
        this.data = data;
    }

    public static <T extends Serializable> UnifiedResp<T> success(T data) {
        return with(SUCCESS, data);
    }

    public static <T extends Serializable> UnifiedResp<T> success() {
        return with(SUCCESS);
    }

    public static <T extends Serializable> UnifiedResp<T> fail(StatusCode code) {
        return with(code);
    }

    public static <T extends Serializable> UnifiedResp<T> fail(String message) {
        return with(INTERNAL_ERROR, message);
    }


    public static <T extends Serializable> UnifiedResp<T> with(StatusCode code) {
        UnifiedResp<T> response = new UnifiedResp<>();
        response.code = code;
        response.message = code.message();
        return response;
    }

    public static <T extends Serializable> UnifiedResp<T> with(StatusCode code, String message) {
        UnifiedResp<T> response = new UnifiedResp<>();
        response.code = code;
        response.message = message;
        return response;
    }

    public static <T extends Serializable> UnifiedResp<T> with(StatusCode code, T data) {
        UnifiedResp<T> response = new UnifiedResp<>();
        response.code = code;
        response.message = code.message();
        response.data = data;
        return response;
    }

    public static <T extends Serializable> UnifiedResp<T> with(StatusCode code, String message, T data) {
        UnifiedResp<T> response = new UnifiedResp<>();
        response.code = code;
        response.message = message;
        response.data = data;
        return response;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}