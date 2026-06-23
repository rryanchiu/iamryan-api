package dev.iamryan.repository;

import dev.iamryan.entity.RadioInfoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RadioInfoRepository extends JpaRepository<RadioInfoEntity, Long> {
    RadioInfoEntity findOneByStationUuid(String stationUuid);

    Slice<RadioInfoEntity> findByGeoLatIsNotNullAndGeoLongIsNotNull(Pageable pageable);

    Slice<RadioInfoEntity> findByGeoLatIsNotNullAndGeoLongIsNotNullAndColorsIsNotNull(Pageable pageable);
}
