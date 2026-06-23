package dev.iamryan.repository;

import dev.iamryan.entity.RadioInfoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RadioInfoRepository extends JpaRepository<RadioInfoEntity, Long> {
    RadioInfoEntity findOneByStationUuid(String stationUuid);

    Slice<RadioInfoEntity> findByGeoLatIsNotNullAndGeoLongIsNotNull(Pageable pageable);

    Slice<RadioInfoEntity> findByGeoLatIsNotNullAndGeoLongIsNotNullAndColorsIsNotNull(Pageable pageable);

    @Query(value = """
            select
                change_uuid as changeUuid,
                station_uuid as stationUuid,
                server_uuid as serverUuid,
                name as name,
                url as url,
                homepage as homepage,
                favicon as favicon,
                tags as tags,
                country as country,
                country_code as countryCode,
                state as state,
                language as language,
                language_codes as languageCodes,
                click_count as clickCount,
                click_trend as clickTrend,
                geo_lat as geoLat,
                geo_long as geoLong,
                geo_distance as geoDistance,
                colors as colors
            from radio_info
            where geo_lat is not null
              and geo_long is not null
            order by click_count desc
            limit :limit offset :offset
            """, nativeQuery = true)
    List<RadioInfoStationProjection> findStationList(@Param("offset") int offset, @Param("limit") int limit);
}
