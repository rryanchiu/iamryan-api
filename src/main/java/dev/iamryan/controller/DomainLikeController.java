package me.rryan.tinyurl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.rryan.tinyurl.model.ResponseResult;
import me.rryan.tinyurl.service.DomainLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
