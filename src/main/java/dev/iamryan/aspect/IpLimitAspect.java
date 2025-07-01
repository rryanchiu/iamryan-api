package me.rryan.tinyurl.aspect;


import jakarta.servlet.http.HttpServletRequest;
import me.rryan.tinyurl.annotation.IpLimit;
import me.rryan.tinyurl.exception.IpLimitException;
import me.rryan.tinyurl.util.HttpServletRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;


@Aspect
@Component
public class IpLimitAspect {


    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Pointcut("@annotation(ipLimit)")
    public void ipLimitPointcut(IpLimit ipLimit) {
    }

    @Before("ipLimitPointcut(ipLimit)")
    public void logBefore(JoinPoint joinPoint, IpLimit ipLimit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = HttpServletRequestUtil.getIpAddress(request);
        // 每分钟限制访问次数
        int limitTime = ipLimit.value();
        // 业务key,用来区分不同业务下的访问次数限制
        String businessKey = ipLimit.key();
        // 构建Redis的key
        String redisKey = buildRedisKey(ip, businessKey);
        long count = 0L;

        // 检查当前时间窗口内的访问次数
        String countStr = (String) redisTemplate.opsForValue().get(redisKey);
        if (!StringUtils.isEmpty(countStr)) {
            count = Long.parseLong(countStr);
        }

        if (count >= limitTime) {
            throw new IpLimitException("Request too frequent, please try again later.");
        }

        // 增加访问次数
        redisTemplate.opsForValue().increment(redisKey, 1L);

        // 设置过期时间，例如每分钟过期
        redisTemplate.expire(redisKey, 1, TimeUnit.MINUTES);
    }

    private String buildRedisKey(String ip, String businessKey) {
        // 构建Redis的key，可以根据实际情况调整格式
        return "ipLimit:" + ip + ":" + businessKey + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
