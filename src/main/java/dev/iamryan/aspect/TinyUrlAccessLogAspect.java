package me.rryan.tinyurl.aspect;

import me.rryan.tinyurl.annotation.TinyUrlAccessLog;
import me.rryan.tinyurl.service.TinyUrlAccessLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class TinyUrlAccessLogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinyUrlAccessLogAspect.class);

    private final TinyUrlAccessLogService tinyUrlAccessLogService;

    private static final ThreadLocal<LocalDateTime> START_TIME_THREAD_LOCAL = new ThreadLocal<>();

    @Autowired
    public TinyUrlAccessLogAspect(TinyUrlAccessLogService tinyUrlAccessLogService) {
        this.tinyUrlAccessLogService = tinyUrlAccessLogService;
    }

    @Before("@annotation(tinyUrlAccessLog)")
    public void logBefore(JoinPoint joinPoint, TinyUrlAccessLog tinyUrlAccessLog) {
        START_TIME_THREAD_LOCAL.set(LocalDateTime.now());
    }

    @AfterReturning(pointcut = "@annotation(tinyUrlAccessLog)", returning = "response")
    public void logAfterSuccess(JoinPoint joinPoint, TinyUrlAccessLog tinyUrlAccessLog, ResponseEntity<Void> response) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length <= 0) {
                LOGGER.error("[TinyUrlAccessLogAspect] Params are empty!");
                return;
            }
            HttpStatusCode status = response.getStatusCode();
            String shortCode = (String) args[0];
            LocalDateTime accessTime = START_TIME_THREAD_LOCAL.get();
            LOGGER.info("[TinyUrlAccessLogAspect] ShortCode: {} ResponseStatus: {}", shortCode, status.value());
            tinyUrlAccessLogService.log(shortCode, status, accessTime);
        } catch (Exception e) {
            LOGGER.error("[TinyUrlAccessLogAspect] Exception: {}", e.getMessage(), e);
        } finally {
            START_TIME_THREAD_LOCAL.remove();
        }
    }

    @AfterThrowing(pointcut = "@annotation(tinyUrlAccessLog)", throwing = "error")
    public void logAfterError(JoinPoint joinPoint, TinyUrlAccessLog tinyUrlAccessLog, Throwable error) {
        System.out.println("访问出错后记录日志: " + joinPoint.getSignature().getName());
        System.out.println("异常信息: " + error.getMessage());
    }
}
