package me.rryan.tinyurl.model.resp;


import java.io.Serializable;
import java.time.LocalDateTime;


public class UrlShortenResp implements Serializable {

    public UrlShortenResp() {
    }

    public UrlShortenResp(String shortUrl, LocalDateTime expireTime, String code) {
        this.shortUrl = shortUrl;
        this.expireTime = expireTime;
        this.code = code;
    }

    private String shortUrl;

    private LocalDateTime expireTime;

    private String code;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
