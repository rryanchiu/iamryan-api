package dev.iamryan.model.resp;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class RadioInfoStationDTO implements Serializable {


    private String changeUuid;
    private String stationUuid;
    private String serverUuid;

    private String name;

    private String url;

    private String homepage;

    private String favicon;

    private String tags;

    private String country;

    private String countryCode;

    private String state;
    private String language;
    private String languageCodes;

    private Integer clickCount;
    private Integer clickTrend;

    private Double geoLat;
    private Double geoLong;
    private Double geoDistance;
    private String colors;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
