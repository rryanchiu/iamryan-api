package me.rryan.tinyurl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "radio_info")
@Getter
@Setter
public class RadioInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String changeUuid;
    private String stationUuid;
    private String serverUuid;

    @Column(length = 2048)
    private String name;

    @Column(columnDefinition = "blob")
    private String url;

    @Column(length = 2048)
    private String urlResolved;

    @Column(length = 2048)
    private String homepage;

    @Column(length = 2048)
    private String favicon;

    @Column(length = 2048)
    private String tags;

    private String country;
    private String countryCode;
    private String iso31662;
    private String state;
    private String language;
    private String languageCodes;

    private Integer votes;

    private LocalDateTime lastChangeTime;
    private LocalDateTime lastCheckTime;
    private LocalDateTime lastCheckOkTime;
    private LocalDateTime lastLocalCheckTime;
    private LocalDateTime clickTimestamp;

    private Integer clickCount;
    private Integer clickTrend;

    private String codec;
    private Integer bitrate;

    private Boolean hls;
    private Boolean lastCheckOk;
    private Boolean sslError;

    private Double geoLat;
    private Double geoLong;
    private Double geoDistance;

    @Column(length = 2048)
    private String colors;

    private Boolean hasExtendedInfo;

    @CreatedDate
    private LocalDateTime createTime;

    @UpdateTimestamp
    private LocalDateTime updateTime;

}
