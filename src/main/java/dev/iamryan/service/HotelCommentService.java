package dev.iamryan.service;

import dev.iamryan.model.AnalyzeCommentRequest;
import dev.iamryan.model.AnalyzeCommentVO;
import dev.iamryan.model.CommentAnalyzePlanVO;
import dev.iamryan.model.ResponseResult;

import java.util.Map;

public interface HotelCommentService {
    ResponseResult<AnalyzeCommentVO> analyzeHotelComment(AnalyzeCommentRequest request);

    CommentAnalyzePlanVO getPlan(String deviceId);

    CommentAnalyzePlanVO recovery(String deviceId, String recoveryCode);

    Map<String, Object> webhook(Map<String, Object> req);

}
