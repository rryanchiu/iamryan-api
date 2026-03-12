package dev.iamryan.repository;

import dev.iamryan.entity.RadioInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RadioInfoRepository extends JpaRepository<RadioInfoEntity, Long> {
    RadioInfoEntity findOneByStationUuid(String stationUuid);

    Page<RadioInfoEntity> findByGeoLatIsNotNullAndGeoLongIsNotNull(Pageable pageable);

    Page<RadioInfoEntity> findByGeoLatIsNotNullAndGeoLongIsNotNullAndColorsIsNotNull(Pageable pageable);
}
