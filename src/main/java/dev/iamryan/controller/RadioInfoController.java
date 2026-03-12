package dev.iamryan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import dev.iamryan.model.ResponseResult;
import dev.iamryan.model.resp.RadioInfoStationDTO;
import dev.iamryan.service.RadioInfoService;
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
