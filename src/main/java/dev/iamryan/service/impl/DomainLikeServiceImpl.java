package me.rryan.tinyurl.service.impl;

import me.rryan.tinyurl.repository.DomainLikeRepository;
import me.rryan.tinyurl.service.DomainLikeService;
import me.rryan.tinyurl.util.DomainLikeBuffer;
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
        return domainLikeRepository.getLikesByDomain(domain);
    }

    @Override
    public void like(String domain) {
        domainLikeBuffer.like(domain);
    }
}
