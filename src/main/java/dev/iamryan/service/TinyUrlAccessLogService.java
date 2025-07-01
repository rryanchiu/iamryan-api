package me.rryan.tinyurl.service;

import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;

public interface TinyUrlAccessLogService {
    boolean log(String code, HttpStatusCode status, LocalDateTime accessTime);

}
