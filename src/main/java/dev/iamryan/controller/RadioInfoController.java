package me.rryan.tinyurl.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.rryan.tinyurl.model.ResponseResult;
import me.rryan.tinyurl.model.resp.RadioInfoStationDTO;
import me.rryan.tinyurl.service.RadioInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "RadioInfoController")
@RestController
@RequestMapping
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class RadioInfoController {

    private final RadioInfoService radioInfoService;

    @Operation(method = "GET")
    @GetMapping("/api/radio/stations")
    public ResponseResult<List<RadioInfoStationDTO>> stations(@RequestParam(required = false) Integer offset,
                                                              @RequestParam(required = false) Integer pageSize
    ) {
        return ResponseResult.success(radioInfoService.stations(offset, pageSize));
    }


}
