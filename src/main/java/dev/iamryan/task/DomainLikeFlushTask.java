package me.rryan.tinyurl.task;


import me.rryan.tinyurl.repository.DomainLikeRepository;
import me.rryan.tinyurl.util.DomainLikeBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
public class DomainLikeFlushTask {

    private final DomainLikeBuffer domainLikeBuffer;
    private final DomainLikeRepository likeRepository;

    @Autowired
    public DomainLikeFlushTask(DomainLikeBuffer domainLikeBuffer, DomainLikeRepository likeRepository) {
        this.domainLikeBuffer = domainLikeBuffer;
        this.likeRepository = likeRepository;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void flushLikes() {
        Map<String, Long> snapshot = domainLikeBuffer.snapshotAndClear();

        for (Map.Entry<String, Long> entry : snapshot.entrySet()) {
            String domain = entry.getKey();
            long likeCount = entry.getValue();
            likeRepository.incrementLikes(domain, likeCount);
        }
    }
}
