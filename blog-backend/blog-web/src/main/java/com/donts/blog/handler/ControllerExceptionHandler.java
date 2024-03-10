package com.donts.blog.handler;

import com.donts.exception.BlogBizException;
import com.donts.response.UnifiedResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局Controller异常处理
 * 打印日志&返回统一结果
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    private static final String LOG_PREFIX = "[通用异常处理]";

    @ExceptionHandler(Exception.class)
    public UnifiedResp<String> handleException(Exception e) {
        log.error("{} 未知异常", LOG_PREFIX, e);
        //TODO 发送通知
        return UnifiedResp.fail(e.getMessage());
    }

    @ExceptionHandler(BlogBizException.class)
    public UnifiedResp<String> handleBlogBizException(BlogBizException e) {
        log.error("{} 业务异常", LOG_PREFIX, e);
        return UnifiedResp.fail(e.getStatusCode());
    }

}
