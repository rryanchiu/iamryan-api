package dev.iamryan.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "dify-ai-client", url = "https://api.dify.ai")
//@FeignClient(name = "dify-ai-client", url = "http://192.168.31.249")
public interface DifyWorkflowClient {

    @PostMapping("/v1/workflows/run")
    Map<String, Object> runWorkflow(@RequestHeader("Authorization") String token,
                                    @RequestBody Map<String, Object> requestBody);
}
