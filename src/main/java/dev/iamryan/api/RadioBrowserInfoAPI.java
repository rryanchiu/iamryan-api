package dev.iamryan.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "radio-browser-info-api", url = "https://de1.api.radio-browser.info")
public interface RadioBrowserInfoAPI {

    @GetMapping("/json/stations")
    String getStations(@RequestParam Integer offset,
                       @RequestParam Integer limit,
                       @RequestParam(name = "hidebroken") Boolean hideBroken,
                       @RequestParam(name = "has_geo_info") Boolean hasGeoInfo,
                       @RequestParam String order,
                       @RequestParam Boolean reverse);
}
