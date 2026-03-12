package dev.iamryan.controller;

import dev.iamryan.annotation.IpLimit;
import dev.iamryan.model.*;
import dev.iamryan.service.HotelCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/hotel-comment")
//@CrossOrigin(origins = {"ebooking.elong.com","ebooking.ctrip.com"})
@CrossOrigin(origins = "*")
public class HotelCommentController {

    @Autowired
    private HotelCommentService hotelCommentService;

    @IpLimit(key = "comment-analyze", value = 30)
    @PostMapping("/analyze")
    public ResponseResult<AnalyzeCommentVO> analyzeHotelComment(@RequestBody AnalyzeCommentRequest request) {
        // 这里调用 HotelCommentService 的方法来处理请求
        // 返回分析结果
        return hotelCommentService.analyzeHotelComment(request);
    }

    @GetMapping("/plan-list")
    public ResponseResult<List<CommentAnalyzePlanGetVO>> planList() {
        return ResponseResult.success(List.of(

//                CommentAnalyzePlanGetVO.builder()
//                        .url("https://ifdian.net/order/create?plan_id=2a37246a555f11f0ad6e5254001e7c00&month=1&product_type=0&custom_order_id=e187bd28-0fbb-4295-93b6-e272410ecdc2")
//                        .title("☕️ 请开发者喝杯咖啡（赠送 100 次分析）")
//                        .key("hotel-comment-vip-1")
//                        .build()
////
//                CommentAnalyzePlanGetVO.builder()
//                        .url("https://ifdian.net/order/create?plan_id=2afa4cec555f11f08b6552540025c377&month=1&product_type=0&custom_order_id=e187bd28-0fbb-4295-93b6-e272410ecdc2")
//                        .title("\uD83D\uDE80 （赠送 500 次分析）")
//                        .key("hotel-comment-vip-2")
//                        .build()
        ));
    }

    @GetMapping("/get-plan")
    public ResponseResult<CommentAnalyzePlanVO> getPlan(@RequestParam String deviceId) {
        return ResponseResult.success(hotelCommentService.getPlan(deviceId));
    }

    @GetMapping("/status")
    public ResponseResult<CommentAnalyzeStatusVO> getStatus() {
        return ResponseResult.success(CommentAnalyzeStatusVO.builder().version("0.0.1 beta").status(1).build());
    }

    @PostMapping("/recover")
    public ResponseResult<CommentAnalyzePlanVO> recovery(@RequestParam String deviceId,
                                                         @RequestParam String recoveryCode) {
        return ResponseResult.success(hotelCommentService.recovery(deviceId, recoveryCode));
    }



    @PostMapping("/afdian-webhook")
    public Map<String,Object> webhook(@RequestBody Map<String, Object> req ) {
        return hotelCommentService.webhook(req);
    }
}
