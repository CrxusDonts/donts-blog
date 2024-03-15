package com.donts.blog.listener;

import com.donts.blog.entity.OperationLog;
import com.donts.blog.event.OperationLogEvent;
import com.donts.blog.service.OperationLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BlogApplicationListener {
    private static final String LOG_PREFIX = "BlogApplicationListener";

    @Resource
    private OperationLogService operationLogService;

    @Async
    @EventListener(OperationLogEvent.class)
    public void saveOperationLog(OperationLogEvent operationLogEvent) {
        log.info(" {} saveOperationLog: {}", LOG_PREFIX, operationLogEvent.getSource());
        operationLogService.save((OperationLog) operationLogEvent.getSource());
    }


}