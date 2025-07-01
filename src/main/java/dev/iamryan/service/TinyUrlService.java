package me.rryan.tinyurl.service;

import me.rryan.tinyurl.model.req.UrlShortenReq;
import me.rryan.tinyurl.model.resp.UrlShortenResp;

public interface TinyUrlService {
    UrlShortenResp createSHortUrl(UrlShortenReq req);

    String getLongUrlByShortCode(String shortCode);
}
