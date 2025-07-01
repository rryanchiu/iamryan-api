package me.rryan.tinyurl.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import me.rryan.tinyurl.entity.TinyUrlAccessLogEntity;
import me.rryan.tinyurl.entity.TinyUrlInfoEntity;
import me.rryan.tinyurl.repository.TinyUrlAccessLogRepository;
import me.rryan.tinyurl.repository.TinyUrlRepository;
import me.rryan.tinyurl.service.TinyUrlAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
public class TinyUrlAccessLogServiceImpl implements TinyUrlAccessLogService {

    private final TinyUrlAccessLogRepository tinyUrlAccessLogRepository;
    private final TinyUrlRepository tinyUrlRepository;

    @Autowired
    public TinyUrlAccessLogServiceImpl(TinyUrlAccessLogRepository tinyUrlAccessLogRepository, TinyUrlRepository tinyUrlRepository) {
        this.tinyUrlAccessLogRepository = tinyUrlAccessLogRepository;
        this.tinyUrlRepository = tinyUrlRepository;
    }

    @Override
    public boolean log(String code, HttpStatusCode status, LocalDateTime accessTime) {
        // 获取 HttpServletRequest 对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取请求的 Headers 信息
        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");
        String userAgent = request.getHeader("User-Agent");

        // 获取访问者 IP
        String ipAddress = request.getRemoteAddr();

        String requestURI = request.getRequestURI();

        TinyUrlInfoEntity entity = tinyUrlRepository.findOneByCode(code);

        TinyUrlAccessLogEntity logEntity = new TinyUrlAccessLogEntity();
        logEntity.setTinyUrlId(entity == null ? 0L : entity.getId());
        logEntity.setStatusCode(status.value());
        logEntity.setAccessTime(accessTime);
        logEntity.setResponseTime(LocalDateTime.now());
        logEntity.setHeaders("");
        logEntity.setReferrer(referer);
        logEntity.setUserAgent(userAgent);
        logEntity.setIp(ipAddress);
        logEntity.setLocation("");
        tinyUrlAccessLogRepository.save(logEntity);
        return true;
    }
}
