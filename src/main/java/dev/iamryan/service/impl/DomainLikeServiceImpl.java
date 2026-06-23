package dev.iamryan.service.impl;

import dev.iamryan.repository.DomainLikeRepository;
import dev.iamryan.service.DomainLikeService;
import dev.iamryan.util.DomainLikeBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class DomainLikeServiceImpl implements DomainLikeService {

    private final DomainLikeRepository domainLikeRepository;
    private final DomainLikeBuffer domainLikeBuffer;

    @Autowired
    public DomainLikeServiceImpl(DomainLikeBuffer domainLikeBuffer, DomainLikeRepository domainLikeRepository) {
        this.domainLikeBuffer = domainLikeBuffer;
        this.domainLikeRepository = domainLikeRepository;
    }

    @Override
    public Long getLikeCount(String domain) {
        Long cachedLikes = domainLikeBuffer.getCachedTotalLikes(domain);
        if (cachedLikes != null) {
            return cachedLikes;
        }
        Long dbLikes = domainLikeRepository.getLikesByDomain(domain);
        long bufferedLikes = domainLikeBuffer.getBufferedLikes(domain);
        long totalLikes = (dbLikes == null ? 0L : dbLikes) + bufferedLikes;
        domainLikeBuffer.cacheTotalLikes(domain, totalLikes);
        return totalLikes;
    }

    @Override
    public void like(String domain) {
        domainLikeBuffer.like(domain);
    }
}
