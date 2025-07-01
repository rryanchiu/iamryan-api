package me.rryan.tinyurl.controller;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.rryan.tinyurl.annotation.IpLimit;
import me.rryan.tinyurl.annotation.TinyUrlAccessLog;
import me.rryan.tinyurl.model.ResponseResult;
import me.rryan.tinyurl.model.req.UrlShortenReq;
import me.rryan.tinyurl.model.resp.UrlShortenResp;
import me.rryan.tinyurl.service.TinyUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "TinyUrlController")
@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class TinyUrlController {


    private final TinyUrlService tinyUrlService;

    @Autowired
    public TinyUrlController(TinyUrlService tinyUrlService) {
        this.tinyUrlService = tinyUrlService;
    }

    @Operation(method = "POST", description = "Create Short Url")
    @PostMapping("/api/tinyurl/shorten")
    @IpLimit(key="shorten",value = 15)
    public ResponseResult<UrlShortenResp> createShortUrl(@RequestBody UrlShortenReq req) {
        UrlShortenResp resp = tinyUrlService.createSHortUrl(req);
        return ResponseResult.success(resp);
    }

    @Operation(method = "GET", description = "Redirect To Long URL")
    @GetMapping("/{shortCode}")
    @TinyUrlAccessLog
    @IpLimit(key="redirect",value = 30)
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable String shortCode) {
        // 假设我们通过 shortCode 获取对应的长链接
        String longUrl = tinyUrlService.getLongUrlByShortCode(shortCode);

        // 如果找不到对应的长链接，可以返回404错误
        if (StringUtils.isEmpty(longUrl)) {
            return ResponseEntity.notFound().build();
        }

        if (!longUrl.startsWith("http")) {
            longUrl = "http://" + longUrl;
        }
        // 返回302重定向到长链接
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }

}
