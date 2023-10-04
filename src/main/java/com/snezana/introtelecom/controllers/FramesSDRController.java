package com.snezana.introtelecom.controllers;

import com.snezana.introtelecom.dto.PackageFrameSaveDTO;
import com.snezana.introtelecom.dto.PackageFrameViewDTO;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.response.RestMessage;
import com.snezana.introtelecom.services.FramesSDRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class FramesSDRController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FramesSDRController.class);

    @Autowired
    private FramesSDRService framesSDRService;

    @Operation(tags = "FramesSDR Controller", summary = "save a new package frame", description = "save a package frame")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/packageFrame/saveNewPackageFrame", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> savePackageFrame(@RequestBody @Valid PackageFrameSaveDTO packageFrameSaveDTO) {
        log.info("in saveNewPackageFrame");
        framesSDRService.saveNewPackageFrame(packageFrameSaveDTO);
        String message = "The new PackageFrame is saved.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "FramesSDR Controller", summary = "Get PackageFrame by Id", description = "Get PackageFrame by Id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFrameById/{packfrId}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<PackageFrameViewDTO>> getPackageFrameById(@PathVariable Long packfrId){
        PackageFrameViewDTO packageFrameViewDTO = framesSDRService.findPackageFrameById(packfrId);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTO));
    }

    @Operation(tags = "FramesSDR Controller", summary = "Get PackageFrame by PhoneNumber, StartTime, EndTime", description = "Get PackageFrame by PhoneNumber, StartTime, EndTime")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFrameByPhoneNumberStartTimeEndTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> getPackageFrameByPhoneNumberStartTimeEndTime(@PathVariable String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrStartDateTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrEndDateTime){
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPhoneNumberStartTimeEndTime(phoneNumber, packfrStartDateTime, packfrEndDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", summary = "Get PackageFrame by PhoneNumber, StartTime", description = "Get PackageFrame by PhoneNumber, StartTime")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFrameByPhoneNumberStartTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> getPackageFrameByPhoneNumberStartTime(@PathVariable String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrStartDateTime){
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPhoneNumberStartTime(phoneNumber, packfrStartDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", summary = "Get PackageFrame by PackageCode, StartTime, EndTime", description = "Get PackageFrame by PackageCode, StartTime, EndTime")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFrameByPackageCodeStartTimeEndTime/{packageCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> getPackageFrameByPackageCodeStartTimeEndTime(@PathVariable String packageCode, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrStartDateTime, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrEndDateTime){
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPackageCodeStartTimeEndTime(packageCode, packfrStartDateTime, packfrEndDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", summary = "Get PackageFrame by PackageCode, StartTime", description = "Get PackageFrame by PackageCode, StartTime")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFrameByPackageCodeStartTime/{packageCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> getPackageFrameByPackageCodeStartTime(@PathVariable String packageCode, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrStartDateTime){
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPackageCodeStartTime(packageCode, packfrStartDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "delete a PackageFrame")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/packageFrame/deletePackageFrame/{packfrId}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> deletePackageFrame(@PathVariable Long packfrId){
        framesSDRService.deletePackageFrame(packfrId);
        String message = "The PackageFrame '" +packfrId + "' is deleted!";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

}
