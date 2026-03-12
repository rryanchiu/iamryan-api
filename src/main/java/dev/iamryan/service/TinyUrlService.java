package dev.iamryan.service;

import dev.iamryan.model.req.UrlShortenReq;
import dev.iamryan.model.resp.UrlShortenResp;

public interface TinyUrlService {
    UrlShortenResp createSHortUrl(UrlShortenReq req);

    String getLongUrlByShortCode(String shortCode);
}
