package com.donts.blog.handler;

import com.donts.exception.BlogBizException;
import com.donts.response.UnifiedResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

import static com.donts.enums.RespStatus.BAD_REQUEST;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public UnifiedResp<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = Optional.of(ex.getBindingResult())
                .map(Errors::getFieldError)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("参数校验失败，但未获取到具体错误信息");
        return UnifiedResp.with(BAD_REQUEST, errorMessage);
    }

}
