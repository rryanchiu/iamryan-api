package me.rryan.tinyurl.service;

public interface DomainLikeService {
    Long getLikeCount(String domain);

    void like(String domain);
}
