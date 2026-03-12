package dev.iamryan.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class DomainLikeBuffer {

    private final AtomicReference<ConcurrentHashMap<String, Long>> likeMapRef =
            new AtomicReference<>(new ConcurrentHashMap<>());

    public void like(String domain) {
        likeMapRef.get().merge(domain, 1L, Long::sum);
    }

    public long getBufferedLikes(String domain) {
        return likeMapRef.get().getOrDefault(domain, 0L);
    }

    public Map<String, Long> snapshotAndClear() {
        // Atomic swap to avoid dropping likes written during flush.
        return new HashMap<>(likeMapRef.getAndSet(new ConcurrentHashMap<>()));
    }
}
