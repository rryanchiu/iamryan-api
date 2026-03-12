package dev.iamryan.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import dev.iamryan.model.AnalyzeCommentRequest;
import dev.iamryan.model.AnalyzeCommentVO;
import dev.iamryan.model.CommentAnalyzePlanVO;
import dev.iamryan.model.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import dev.iamryan.api.DifyWorkflowClient;
import dev.iamryan.entity.CommentAnalyzePlanEntity;
import dev.iamryan.exception.BusinessException;
import dev.iamryan.repository.CommentAnalyzePlanRepository;
import dev.iamryan.service.HotelCommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class HotelCommentServiceImpl implements HotelCommentService {
    @Autowired
    private DifyWorkflowClient difyWorkflowClient;
    @Autowired
    private CommentAnalyzePlanRepository commentAnalyzePlanRepository;

    //    private String difyAiToken=  "Bearer app-mfhcrBkKvirxIcMrNj4iQ5Gk";
//    private static final String DIFY_AI_TOKEN = "Bearer app-NhoMfXfNL8hHcJ0qvmjjXyd5";
    private static final String DIFY_AI_TOKEN = "Bearer app-vRLvVC4csuXi7uo8dFsoJLkC";
    private static final ConcurrentHashMap<String, Object> deviceLocks = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public ResponseResult<AnalyzeCommentVO> analyzeHotelComment(AnalyzeCommentRequest request) {

        if (StringUtils.isEmpty(request.getDeviceId())) {
            return ResponseResult.error(500, "设备ID不能为空");
        }

        String deviceId = request.getDeviceId();
        Object lock = deviceLocks.computeIfAbsent(deviceId, k -> new Object());
        synchronized (lock) {
            try {
                if (StringUtils.isEmpty(request.getContent())) {
                    return ResponseResult.error(400, "评论内容不能为空");
                }
                CommentAnalyzePlanVO commentAnalyzePlanVO = getPlan(request.getDeviceId());
                if (commentAnalyzePlanVO.getTodayUsedCount() >= commentAnalyzePlanVO.getTotalLimit()) {
                    return ResponseResult.error(403, "分析次数已用完");
                }

                // 占用次数
                commentAnalyzePlanRepository.incrementTodayUsedCount(request.getDeviceId(), LocalDate.now(), LocalDateTime.now());

                Map<String, Object> input = Map.of(
                        "inputs", Map.of("content", request.getContent()),
                        "response_mode", "blocking",
                        "user", "sfeel-user"
                );

                Map<String, Object> resp = difyWorkflowClient.runWorkflow(DIFY_AI_TOKEN, input);
                JSONObject jsonObject = new JSONObject(resp);
                if (!jsonObject.containsKey("data")) {
                    return null;
                }
                jsonObject = jsonObject.getJSONObject("data");
                if (!jsonObject.containsKey("status") || !"succeeded".equals(jsonObject.getString("status"))) {
                    return null;
                }
                jsonObject = jsonObject.getJSONObject("outputs");
                ResponseResult<AnalyzeCommentVO> responseResult;
                if (jsonObject.containsKey("code") && jsonObject.containsKey("msg")) {
                    responseResult = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<>() {
                    });
                } else {
                    responseResult = ResponseResult.error(500, "服务异常，请稍后再试");
                }

                if (!Integer.valueOf(0).equals(responseResult.getCode())) {
                    // 如果分析失败，回滚占用次数
                    commentAnalyzePlanRepository.decrementTodayUsedCount(request.getDeviceId(), LocalDate.now(), LocalDateTime.now());
                }
                return responseResult;
            } finally {
                deviceLocks.remove(deviceId);
            }
        }
    }

    @Override
    public CommentAnalyzePlanVO getPlan(String deviceId) {
        CommentAnalyzePlanEntity commentAnalyzePlanEntity = commentAnalyzePlanRepository.findTopByDeviceIdAndDateOrderByCreatedAtDesc(deviceId, LocalDate.now());
        if (commentAnalyzePlanEntity == null) {

            commentAnalyzePlanEntity = CommentAnalyzePlanEntity.builder()
                    .deviceId(deviceId)
                    .date(LocalDate.now())
                    .planId("beta")
                    .month(1)
                    .totalAmount(BigDecimal.ZERO)
                    .showAmount(BigDecimal.ZERO)
                    .planName("beta")
                    .recoveryCode(deviceId)
                    .outTradeNo(deviceId)
                    .userId(deviceId)
                    .remark("")
                    .totalLimit(100)
                    .isBeta(true)
                    .todayUsedCount(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            commentAnalyzePlanEntity = commentAnalyzePlanRepository.save(commentAnalyzePlanEntity);
        }

        return CommentAnalyzePlanVO.builder()
                .date(commentAnalyzePlanEntity.getDate())
                .planName(commentAnalyzePlanEntity.getPlanName())
                .totalLimit(commentAnalyzePlanEntity.getTotalLimit())
                .isBeta(commentAnalyzePlanEntity.getIsBeta())
                .todayUsedCount(commentAnalyzePlanEntity.getTodayUsedCount())
                .build();
    }

    @Override
    public CommentAnalyzePlanVO recovery(String deviceId, String recoveryCode) {
        if (recoveryCode.equals(deviceId)) {
            throw new BusinessException("恢复码错误");
        }
        CommentAnalyzePlanEntity commentAnalyzePlanEntity = commentAnalyzePlanRepository.findTopByRecoveryCode(recoveryCode);
        if (commentAnalyzePlanEntity == null) {
            throw new BusinessException("恢复码错误");
        }
        if (commentAnalyzePlanEntity.getDeviceId().equals(deviceId)) {
            throw new BusinessException("订阅生效中");
        }

        CommentAnalyzePlanEntity commentAnalyzePlanEntityNew = CommentAnalyzePlanEntity.builder()
                .deviceId(deviceId)
                .date(LocalDate.now())
                .planId(commentAnalyzePlanEntity.getPlanId())
                .month(commentAnalyzePlanEntity.getMonth())
                .totalAmount(commentAnalyzePlanEntity.getTotalAmount())
                .showAmount(commentAnalyzePlanEntity.getShowAmount())
                .planName(commentAnalyzePlanEntity.getPlanName())
                .recoveryCode(recoveryCode)
                .outTradeNo(commentAnalyzePlanEntity.getOutTradeNo())
                .userId(commentAnalyzePlanEntity.getUserId())
                .remark(commentAnalyzePlanEntity.getRemark())
                .isBeta(false)
                .totalLimit(commentAnalyzePlanEntity.getTotalLimit())
                .todayUsedCount(commentAnalyzePlanEntity.getTodayUsedCount())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        commentAnalyzePlanRepository.save(commentAnalyzePlanEntityNew);

        return CommentAnalyzePlanVO.builder()
                .date(commentAnalyzePlanEntityNew.getDate())
                .planName(commentAnalyzePlanEntityNew.getPlanName())
                .totalLimit(commentAnalyzePlanEntityNew.getTotalLimit())
                .todayUsedCount(commentAnalyzePlanEntityNew.getTodayUsedCount())
                .build();
    }

    @Override
    public Map<String, Object> webhook(Map<String, Object> req) {
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(req));
        if (!jsonObject.containsKey("ec") || !Integer.valueOf(200).equals(jsonObject.getInteger("ec"))) {
            return Map.of("ec", 200);
        }
        JSONObject data = jsonObject.getJSONObject("data");
        if (!data.containsKey("type") || !"order".equals(data.getString("type"))) {
            return Map.of("ec", 200);
        }
        String outTradeNo = data.getString("out_trade_no");
        String userId = data.getString("user_id");
        String planId = data.getString("plan_id");
        String remark = data.getString("remark");
        Integer month = data.getInteger("month");
        String totalAmount = data.getString("total_amount");

        if (StringUtils.isEmpty(planId)) {
            return Map.of("ec", 200);
        }

//        {
//            "ec": 200,
//                "em": "ok",
//                "data": {
//            "type": "order",
//                    "order": {
//                "out_trade_no": "202106232138371083454010626",
//                        "user_id": "adf397fe8374811eaacee52540025c377",
//                        "plan_id": "a45353328af911eb973052540025c377",
//                        "month": 1,
//                        "total_amount": "5.00",
//                        "show_amount": "5.00",
//                        "status": 2,
//                        "remark": "",
//                        "redeem_id": "",
//                        "product_type": 0,
//                        "discount": "0.00",
//                        "sku_detail": [],
//                "address_person": "",
//                        "address_phone": "",
//                        "address_address": ""
//            }
//        }
//        }
        return Map.of("ec", 200);
    }

}

