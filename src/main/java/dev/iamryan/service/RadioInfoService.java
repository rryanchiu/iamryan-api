package dev.iamryan.service;

import com.alibaba.fastjson2.JSONObject;
import dev.iamryan.model.resp.RadioInfoStationDTO;

import java.util.List;

public interface RadioInfoService {
    void saveOrUpdateStation(JSONObject station);

    List<RadioInfoStationDTO> stations(Integer offset, Integer pageSize);

    void extratStationColor(String stationUuid, String favicon);
}
