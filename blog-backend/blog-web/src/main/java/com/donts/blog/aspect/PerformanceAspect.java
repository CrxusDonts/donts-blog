package com.donts.blog.aspect;

import com.donts.response.UnifiedResp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * controller 性能监控切面
 */
@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    private static final String LOG_PREFIX = "PerformanceAspect";

    @Pointcut("execution(* com.donts..controller..*(..))")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            Object result = joinPoint.proceed();
            stopWatch.stop();
            long executionTime = stopWatch.getTotalTimeMillis();

            log.info("{} -- {} executed in {} ms", LOG_PREFIX, joinPoint.getSignature(), executionTime);

            // If the result is an instance of UnifiedResp, set the costTime field
            if (result instanceof UnifiedResp) {
                ((UnifiedResp<?>) result).setCostTime(executionTime);
            }

            return result;
        } finally {
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
        }
    }
}
