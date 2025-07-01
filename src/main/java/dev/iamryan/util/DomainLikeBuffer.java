package me.rryan.tinyurl.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DomainLikeBuffer {

    private final Map<String, Long> likeMap = new ConcurrentHashMap<>();

    public void like(String domain) {
        likeMap.merge(domain, 1L, Long::sum);
    }


    public Map<String, Long> snapshotAndClear() {
        // 创建快照
        Map<String, Long> snapshot = new HashMap<>(likeMap);
        // 清空缓存
        likeMap.clear();
        return snapshot;
    }
}
