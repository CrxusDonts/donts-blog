package com.donts.response;

import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

import static com.donts.enums.RespStatus.INTERNAL_ERROR;
import static com.donts.enums.RespStatus.SUCCESS;

@Data
@Schema(description = "统一响应")
public final class UnifiedResp<T extends Serializable> implements Serializable {
    @Schema(name = "code", description = "响应码", type = "StatusCode")
    private StatusCode code;
    @Schema(name = "message", description = "响应消息", type = "String")
    private String message;
    @Schema(name = "data", description = "响应数据", type = "模板类型")
    private T data;
    @Schema(name = "costTime", description = "耗时", type = "Long")
    private Long costTime;

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