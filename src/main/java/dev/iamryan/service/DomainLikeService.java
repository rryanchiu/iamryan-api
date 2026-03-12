package dev.iamryan.service;

public interface DomainLikeService {
    Long getLikeCount(String domain);

    void like(String domain);
}
