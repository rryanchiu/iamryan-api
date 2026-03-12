package dev.iamryan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import dev.iamryan.model.ResponseResult;
import dev.iamryan.service.DomainLikeService;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Tag(name = "DomainLikeController")
@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class DomainLikeController {

    private final DomainLikeService domainLikeService;

    @Autowired
    public DomainLikeController(DomainLikeService domainLikeService) {
        this.domainLikeService = domainLikeService;
    }

    @Operation(method = "GET", description = "Total likes")
    @GetMapping("/api/domain/getLikeCount")
    public ResponseResult<Long> totalLikes(@RequestParam String domain) {
        return ResponseResult.success(domainLikeService.getLikeCount(domain));
    }

    @Operation(method = "POST", description = "like")
    @PostMapping("/api/domain/like")
    public ResponseResult<Void> createShortUrl(@RequestParam String domain) {
        domainLikeService.like(domain);
        return ResponseResult.success();
    }

}
