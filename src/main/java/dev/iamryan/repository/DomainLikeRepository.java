package dev.iamryan.repository;

import dev.iamryan.entity.DomainLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainLikeRepository extends JpaRepository<DomainLikeEntity, Long> {

    @Modifying
    @Query("update DomainLikeEntity set likes = likes + ?2 where domain = ?1")
    int incrementLikes(String domain, long likeCount);

    @Query("select likes from DomainLikeEntity where domain = ?1")
    Long getLikesByDomain(String domain);
}
