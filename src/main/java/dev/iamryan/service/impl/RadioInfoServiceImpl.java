package dev.iamryan.service.impl;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import dev.iamryan.entity.RadioInfoEntity;
import dev.iamryan.model.resp.RadioInfoStationDTO;
import dev.iamryan.repository.RadioInfoRepository;
import dev.iamryan.repository.RadioInfoStationProjection;
import dev.iamryan.service.RadioInfoService;
import dev.iamryan.util.ImageColorExtractor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RadioInfoServiceImpl implements RadioInfoService {

    private static final int LEGACY_TEXT_COLUMN_LIMIT = 2048;
    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int MAX_PAGE_SIZE = 500;

    private final RadioInfoRepository radioInfoRepository;

    @Override
    public void saveOrUpdateStation(JSONObject station) {
        String stationUuid = station.getString("stationuuid");
        if (stationUuid == null) {
            return;
        }

        RadioInfoEntity entity = radioInfoRepository.findOneByStationUuid(stationUuid);

        // 解析 lastchangetime
        String incomingChangeTimeStr = station.getString("lastchangetime");
        if (incomingChangeTimeStr == null) {
            return;
        }

        LocalDateTime incomingChangeTime = parseTime(incomingChangeTimeStr);
        if (incomingChangeTime == null) {
            return; // 解析失败
        }

        // 如果存在记录，比较是否需要更新
        if (entity != null) {
            LocalDateTime dbChangeTime = entity.getLastChangeTime();
            if (dbChangeTime != null && !incomingChangeTime.isAfter(dbChangeTime)) {
                return; // 没有更新，跳过
            }
        } else {
            entity = new RadioInfoEntity();
            entity.setStationUuid(stationUuid);
            entity.setCreateTime(LocalDateTime.now());
        }

        // 更新字段
        entity.setChangeUuid(station.getString("changeuuid"));
        entity.setServerUuid(station.getString("serveruuid"));
        entity.setName(truncate(station.getString("name")));
        entity.setUrl(station.getString("url"));
        entity.setUrlResolved("");
        entity.setHomepage(truncate(station.getString("homepage")));
        entity.setFavicon(truncate(station.getString("favicon")));
        entity.setTags(truncate(station.getString("tags")));
        entity.setCountry(station.getString("country"));
        entity.setCountryCode(station.getString("countrycode"));
        entity.setState(station.getString("state"));
        entity.setLanguage(station.getString("language"));
        entity.setLanguageCodes(station.getString("languagecodes"));
        entity.setIso31662(station.getString("iso_3166_2"));
        entity.setVotes(station.getInteger("votes"));
        entity.setCodec(station.getString("codec"));
        entity.setBitrate(station.getInteger("bitrate"));
        entity.setHls(station.getInteger("hls") == 1);
        entity.setLastCheckOk(station.getInteger("lastcheckok") == 1);
        entity.setClickCount(station.getInteger("clickcount"));
        entity.setClickTrend(station.getInteger("clicktrend"));
        entity.setClickTrend(station.getInteger("clicktrend"));
        entity.setGeoLong(station.getDouble("geo_long"));
        entity.setGeoLat(station.getDouble("geo_lat"));
        entity.setGeoDistance(station.getDouble("geo_distance"));
        entity.setHasExtendedInfo(station.getBoolean("has_extended_info"));
        entity.setLastChangeTime(incomingChangeTime);
//        if (!StringUtils.isEmpty(entity.getFavicon()) && StringUtils.isEmpty(entity.getColors())) {
//            try {
//                entity.setColors(ImageColorExtractor.extractDominantColors(entity.getFavicon()).getColorsString());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        radioInfoRepository.save(entity);
    }

    @Override
    public List<RadioInfoStationDTO> stations(Integer offset, Integer pageSize) {
        int safePageSize = pageSize == null || pageSize <= 0 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
        int safeOffset = offset == null || offset < 0 ? 0 : offset;

        return radioInfoRepository.findStationList(safeOffset, safePageSize).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void extratStationColor(String stationUuid, String favicon) {
        RadioInfoEntity entity = radioInfoRepository.findOneByStationUuid(stationUuid);
        if (entity == null) {
            return;
        }
        if (!StringUtils.isEmpty(entity.getFavicon()) && StringUtils.isEmpty(entity.getColors())) {
            try {
                entity.setColors(ImageColorExtractor.extractDominantColors(entity.getFavicon()).getColorsString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(entity.getName() + " " + entity.getColors());
        radioInfoRepository.save(entity);
    }

    private RadioInfoStationDTO convertToDTO(RadioInfoEntity entity) {
        RadioInfoStationDTO dto = new RadioInfoStationDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    private RadioInfoStationDTO convertToDTO(RadioInfoStationProjection projection) {
        RadioInfoStationDTO dto = new RadioInfoStationDTO();
        dto.setChangeUuid(projection.getChangeUuid());
        dto.setStationUuid(projection.getStationUuid());
        dto.setServerUuid(projection.getServerUuid());
        dto.setName(projection.getName());
        dto.setUrl(projection.getUrl());
        dto.setHomepage(projection.getHomepage());
        dto.setFavicon(projection.getFavicon());
        dto.setTags(projection.getTags());
        dto.setCountry(projection.getCountry());
        dto.setCountryCode(projection.getCountryCode());
        dto.setState(projection.getState());
        dto.setLanguage(projection.getLanguage());
        dto.setLanguageCodes(projection.getLanguageCodes());
        dto.setClickCount(projection.getClickCount());
        dto.setClickTrend(projection.getClickTrend());
        dto.setGeoLat(projection.getGeoLat());
        dto.setGeoLong(projection.getGeoLong());
        dto.setGeoDistance(projection.getGeoDistance());
        dto.setColors(projection.getColors());
        return dto;
    }


    private boolean isNewer(String incomingTime, String existingTime) {
        try {
            // 假设时间格式是 "yyyy-MM-dd HH:mm:ss"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime incoming = LocalDateTime.parse(incomingTime, formatter);
            LocalDateTime existing = LocalDateTime.parse(existingTime, formatter);
            return incoming.isAfter(existing);
        } catch (Exception e) {
            // 解析失败保守处理：认为是需要更新
            return true;
        }
    }

    private LocalDateTime parseTime(String timeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(timeStr, formatter);
        } catch (Exception e) {
            // 可以打印日志
            return null;
        }
    }

    private String truncate(String value) {
        if (value == null || value.length() <= LEGACY_TEXT_COLUMN_LIMIT) {
            return value;
        }
        return value.substring(0, LEGACY_TEXT_COLUMN_LIMIT);
    }

}
