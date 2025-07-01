package me.rryan.tinyurl.model.req;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public class UrlShortenReq implements Serializable {

    @NotEmpty(message = "{message.notEmpty.longUrl}")
    private String longUrl;

    private Integer expireSeconds;

    private String code;

    public @NotEmpty(message = "{message.notEmpty.longUrl}") String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(@NotEmpty(message = "{message.notEmpty.longUrl}") String longUrl) {
        this.longUrl = longUrl;
    }

    public Integer getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
