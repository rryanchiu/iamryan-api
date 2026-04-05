package dev.iamryan.aspect;


import jakarta.servlet.http.HttpServletRequest;
import dev.iamryan.annotation.IpLimit;
import dev.iamryan.exception.IpLimitException;
import dev.iamryan.util.HttpServletRequestUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.TimeUnit;


@Aspect
@Component
public class IpLimitAspect {

    private static final long WINDOW_MILLIS = TimeUnit.MINUTES.toMillis(1);
    private static final long CLEANUP_INTERVAL = 1000L;
    private static final Map<String, Counter> COUNTERS = new ConcurrentHashMap<>();
    private static final AtomicLong REQUEST_SEQUENCE = new AtomicLong(0L);


    @Pointcut("@annotation(ipLimit)")
    public void ipLimitPointcut(IpLimit ipLimit) {
    }

    @Before("ipLimitPointcut(ipLimit)")
    public void logBefore(JoinPoint joinPoint, IpLimit ipLimit) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = HttpServletRequestUtil.getIpAddress(request);
        // 每分钟限制访问次数
        int limitTime = ipLimit.value();
        // 业务key,用来区分不同业务下的访问次数限制
        String businessKey = ipLimit.key();

        String counterKey = buildCounterKey(ip, businessKey);
        long currentWindow = currentMinuteWindow(System.currentTimeMillis());
        AtomicBoolean limited = new AtomicBoolean(false);

        COUNTERS.compute(counterKey, (key, current) -> {
            if (current == null || current.windowStartMillis != currentWindow) {
                return new Counter(currentWindow, 1);
            }
            if (current.count >= limitTime) {
                limited.set(true);
                return current;
            }
            return new Counter(current.windowStartMillis, current.count + 1);
        });

        if (limited.get()) {
            throw new IpLimitException("Request too frequent, please try again later.");
        }

        long sequence = REQUEST_SEQUENCE.incrementAndGet();
        if (sequence % CLEANUP_INTERVAL == 0) {
            cleanupExpiredCounters(currentWindow);
        }
    }

    private String buildCounterKey(String ip, String businessKey) {
        return ip + ":" + businessKey;
    }

    private long currentMinuteWindow(long nowMillis) {
        return nowMillis / WINDOW_MILLIS * WINDOW_MILLIS;
    }

    private void cleanupExpiredCounters(long currentWindow) {
        COUNTERS.entrySet().removeIf(entry -> entry.getValue().windowStartMillis < currentWindow);
    }

    private static final class Counter {
        private final long windowStartMillis;
        private final int count;

        private Counter(long windowStartMillis, int count) {
            this.windowStartMillis = windowStartMillis;
            this.count = count;
        }
    }
}
