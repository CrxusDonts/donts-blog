package com.donts.blog.aop.aspect;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.donts.blog.aop.anno.OptLog;
import com.donts.blog.entity.OperationLog;
import com.donts.blog.event.OperationLogEvent;
import com.donts.blog.utils.IpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class OperationLogAspect {
    private static final String LOG_PREFIX = "OperationLogAspect";
    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private IpUtil ipUtil;

    @Pointcut("@annotation(com.donts.blog.aop.anno.OptLog)")
    public void operationLogPointCut() {
    }

    @AfterReturning(value = "operationLogPointCut()", returning = "keys")
    @SuppressWarnings("unchecked")
    public void saveOperationLog(JoinPoint joinPoint, Object keys) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request =
                (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        OperationLog operationLog = new OperationLog();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Tag controllerTag = (Tag) signature.getDeclaringType().getAnnotation(Tag.class);
        Operation operation = method.getAnnotation(Operation.class);
        OptLog optLog = method.getAnnotation(OptLog.class);
        //操作模块
        operationLog.setOptModule(controllerTag.description());
        //操作类型
        operationLog.setOptType(optLog.optType().getDescription());
        //操作描述
        operationLog.setOptDesc(operation.summary());
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        methodName = className + "." + methodName;
        operationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        operationLog.setOptMethod(methodName);
        if (joinPoint.getArgs().length > 0) {
            if (joinPoint.getArgs()[0] instanceof MultipartFile) {
                operationLog.setRequestParam("file");
            } else {
                operationLog.setRequestParam(JSONUtil.toJsonStr(joinPoint.getArgs()));
            }
        }
        operationLog.setResponseData(JSONUtil.toJsonStr(keys));
        operationLog.setUserId(getUserId());
        String ipAddress = ipUtil.getIpAddress(request);
        operationLog.setIpAddress(ipAddress);
        operationLog.setIpSource(ipUtil.getIpSource(ipAddress));
        operationLog.setOptUri(request.getRequestURI());
        // 发布事件,避免阻塞
        applicationContext.publishEvent(new OperationLogEvent(operationLog));
    }

    private Long getUserId() {
        try {
            return StpUtil.getLoginIdAsLong();
        } catch (Exception e) {
            log.error("{} getUserId exception:", LOG_PREFIX, e);
            return -1L;
        }
    }

}