package dev.iamryan.repository;

public interface RadioInfoStationProjection {
    String getChangeUuid();

    String getStationUuid();

    String getServerUuid();

    String getName();

    String getUrl();

    String getHomepage();

    String getFavicon();

    String getTags();

    String getCountry();

    String getCountryCode();

    String getState();

    String getLanguage();

    String getLanguageCodes();

    Integer getClickCount();

    Integer getClickTrend();

    Double getGeoLat();

    Double getGeoLong();

    Double getGeoDistance();

    String getColors();
}
