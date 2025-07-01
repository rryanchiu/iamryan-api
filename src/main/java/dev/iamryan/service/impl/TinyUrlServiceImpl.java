package me.rryan.tinyurl.service.impl;

import me.rryan.tinyurl.entity.TinyUrlInfoEntity;
import me.rryan.tinyurl.exception.RandomCodeExistsException;
import me.rryan.tinyurl.model.req.UrlShortenReq;
import me.rryan.tinyurl.model.resp.UrlShortenResp;
import me.rryan.tinyurl.repository.TinyUrlRepository;
import me.rryan.tinyurl.service.TinyUrlService;
import me.rryan.tinyurl.util.RandomStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class TinyUrlServiceImpl implements TinyUrlService {

    private final TinyUrlRepository tinyUrlRepository;

    @Value(value = "${tinyurl.prefix}")
    private String tinyurlPrefix;

    @Value(value = "${tinyurl.expireSeconds}")
    private Integer expireSeconds;

    @Autowired
    public TinyUrlServiceImpl(TinyUrlRepository tinyUrlRepository) {
        this.tinyUrlRepository = tinyUrlRepository;
    }


    @Override
    @Retryable(retryFor = RandomCodeExistsException.class, maxAttempts = 5, backoff = @Backoff(delay = 120))
    public UrlShortenResp createSHortUrl(UrlShortenReq req) {
        String code = RandomStringUtil.random(7);
        TinyUrlInfoEntity entity = tinyUrlRepository.findOneByCode(code);

        if (Objects.nonNull(entity)) {
            throw new RandomCodeExistsException();
        }

        int shortUrlExpireSeconds = Objects.requireNonNullElse(req.getExpireSeconds(), expireSeconds);
        String shortUrl = tinyurlPrefix + code;
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime expireTime = createTime.plusSeconds(shortUrlExpireSeconds);

        entity = new TinyUrlInfoEntity();
        entity.setCode(code);
        entity.setCreator("");
        entity.setExpireTime(expireTime);
        entity.setCreateTime(createTime);
        entity.setLongUrl(req.getLongUrl());
        entity.setShortUrl(shortUrl);
        tinyUrlRepository.save(entity);

        return new UrlShortenResp(shortUrl, expireTime, code);
    }

    @Override
    public String getLongUrlByShortCode(String shortCode) {
        TinyUrlInfoEntity entity = tinyUrlRepository.findOneByCode(shortCode);
        if (Objects.isNull(entity)) {
            return null;
        }
        if (LocalDateTime.now().isAfter(entity.getExpireTime())) {
            return null;
        }
        return entity.getLongUrl();
    }
}
;