package com.donts.exception;

import com.donts.response.StatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.donts.enums.RespStatus.INTERNAL_ERROR;


@EqualsAndHashCode(callSuper = true)
@Data
public class BlogBizException extends RuntimeException {

    private final StatusCode statusCode;

    public BlogBizException(String message) {
        super(message);
        this.statusCode = INTERNAL_ERROR;
    }

    public BlogBizException(StatusCode statusCode) {
        super(statusCode.message());
        this.statusCode = statusCode;
    }

    public BlogBizException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public BlogBizException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = INTERNAL_ERROR;

    }

    public BlogBizException(Throwable cause) {
        super(cause);
        this.statusCode = INTERNAL_ERROR;
    }

    protected BlogBizException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = INTERNAL_ERROR;
    }
}
