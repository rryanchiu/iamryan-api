package me.rryan.tinyurl.service;

import me.rryan.tinyurl.model.*;

import java.util.Map;

public interface HotelCommentService {
    ResponseResult<AnalyzeCommentVO> analyzeHotelComment(AnalyzeCommentRequest request);

    CommentAnalyzePlanVO getPlan(String deviceId);

    CommentAnalyzePlanVO recovery(String deviceId, String recoveryCode);

    Map<String, Object> webhook(Map<String, Object> req);

}
