package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.response.RestMessage;
import com.snezana.introtelecom.service.FramesSDRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
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
@RequiredArgsConstructor
public class FramesSDRController {

//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FramesSDRController.class);

    private final FramesSDRService framesSDRService;

    @Operation(tags = "FramesSDR Controller", description = "Get package frame by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFrameById/{packfrId}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<PackageFrameViewDTO>> getPackageFrameById(@PathVariable Long packfrId){
        PackageFrameViewDTO packageFrameViewDTO = framesSDRService.findPackageFrameById(packfrId);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTO));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get package frame by phone number, start time, end time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFramesByPhoneNumberStartTimeEndTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> getPackageFrameByPhoneNumberStartTimeEndTime(@PathVariable String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrStartDateTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrEndDateTime){
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPhoneNumberStartTimeEndTime(phoneNumber, packfrStartDateTime, packfrEndDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get package frame by phone number, start time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFramesByPhoneNumberStartTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> getPackageFrameByPhoneNumberStartTime(@PathVariable String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrStartDateTime){
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPhoneNumberStartTime(phoneNumber, packfrStartDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get package frame by package code, start time, end time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFramesByPackageCodeStartTimeEndTime/{packageCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> getPackageFrameByPackageCodeStartTimeEndTime(@PathVariable String packageCode, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrStartDateTime, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrEndDateTime){
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPackageCodeStartTimeEndTime(packageCode, packfrStartDateTime, packfrEndDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get package frame by package code, start time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "packageFrame/getPackageFramesByPackageCodeStartTime/{packageCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackageFrameViewDTO>>> getPackageFrameByPackageCodeStartTime(@PathVariable String packageCode, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime packfrStartDateTime){
        List<PackageFrameViewDTO> packageFrameViewDTOList = framesSDRService.findPackageFramesByPackageCodeStartTime(packageCode, packfrStartDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(packageFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Change package frame status")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/packageFrame/changePackageFrameStatus/{packfrId}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changePackageFrameStatus(@PathVariable Long packfrId){
        framesSDRService.changePackageFrameStatus(packfrId);
        String message = "The paackage frame status is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "FramesSDR Controller", description = "Delete package frame")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/packageFrame/deletePackageFrame/{packfrId}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> deletePackageFrame(@PathVariable Long packfrId){
        framesSDRService.deletePackageFrame(packfrId);
        String message = "The package frame '" +packfrId + "' is deleted!";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }


    @Operation(tags = "FramesSDR Controller", description = "Save a new addon frame")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/addon/saveNewAddonFrame", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> saveAddonFrame(@RequestBody @Valid AddonFrameSaveDTO addonFrameSaveDTO) {
        framesSDRService.saveNewAddonFrame(addonFrameSaveDTO);
        String message = "The new addon frame is saved.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get addon frame by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addonFrame/getAddonFrameById/{addfrId}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<AddonFrameViewDTO>> getAddonFrameById(@PathVariable Long addfrId){
        AddonFrameViewDTO addonFrameViewDTO = framesSDRService.findAddonFrameById(addfrId);
        return ResponseEntity.ok(RestAPIResponse.of(addonFrameViewDTO));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get addon frame by phone number, start time, end time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addonFrame/getAddonFramesByPhoneNumberStartTimeEndTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AddonFrameViewDTO>>> getAddonFrameByPhoneNumberStartTimeEndTime(@PathVariable String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addfrStartDateTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addfrEndDateTime){
        List<AddonFrameViewDTO> addonFrameViewDTOList = framesSDRService.findAddonFramesByPhoneNumberStartTimeEndTime(phoneNumber, addfrStartDateTime, addfrEndDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(addonFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get addon frame by phone number, start time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addonFrame/getAddonFramesByPhoneNumberStartTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AddonFrameViewDTO>>> getAddonFrameByPhoneNumberStartTime(@PathVariable String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addfrStartDateTime){
        List<AddonFrameViewDTO> addonFrameViewDTOList = framesSDRService.findAddonFramesByPhoneNumberStartTime(phoneNumber, addfrStartDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(addonFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get addon frame by addon code, start time, end time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addonFrame/getAddonFramesByAddonCodeStartTimeEndTime/{addonCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AddonFrameViewDTO>>> getAddonFrameByAddonCodeStartTimeEndTime(@PathVariable String addonCode, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addfrStartDateTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addfrEndDateTime){
        List<AddonFrameViewDTO> addonFrameViewDTOList = framesSDRService.findAddonFramesByAddonCodeStartTimeEndTime(addonCode, addfrStartDateTime, addfrEndDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(addonFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get addon frame by addon code, start time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addonFrame/getAddonFramesByAddonCodeStartTime/{addonCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AddonFrameViewDTO>>> getAddonFrameByAddonCodeStartTime(@PathVariable String addonCode, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime addfrStartDateTime){
        List<AddonFrameViewDTO> addonFrameViewDTOList = framesSDRService.findAddonFramesByAddonCodeStartTime(addonCode, addfrStartDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(addonFrameViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Change addon frame status")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/addonFrame/changeAddonFrameStatus/{addfrId}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changeAddonFrameStatus(@PathVariable Long addfrId){
        framesSDRService.changeAddonFrameStatus(addfrId);
        String message = "The addon frame status is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "FramesSDR Controller", description = "Delete addon frame")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/addonFrame/deleteAddonFrame/{addfrId}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> deleteAddonFrame(@PathVariable Long addfrId){
        framesSDRService.deleteAddonFrame(addfrId);
        String message = "The addon frame '" +addfrId + "' is deleted!";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Operation(tags = "FramesSDR Controller", description = "Save a ServiceDetailRecord")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping(value = "/sdr/saveNewServiceDetailRecord", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> saveServiceDetailRecord(@RequestBody @Valid ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO) {
        String message = framesSDRService.saveNewServiceDetailRecord(serviceDetailRecordSaveDTO);
        if (message.equals("Not EOS")) {
            message = "The new Service Detail Record is saved.";
        }
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get ServiceDetailRecord by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "sdr/getServiceDetailRecordById/{sdrId}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<ServiceDetailRecordViewDTO>> getServiceDetailRecordById(@PathVariable Long sdrId){
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO = framesSDRService.findServiceDetailRecordById(sdrId);
        return ResponseEntity.ok(RestAPIResponse.of(serviceDetailRecordViewDTO));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get ServiceDetailRecords by phone number, start time, end time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "sdr/getServiceDetailRecordsByPhoneNumberStartTimeEndTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<ServiceDetailRecordViewDTO>>> getServiceDetailRecordByPhoneNumberStartTimeEndTime(@PathVariable String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sdrStartDateTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sdrEndDateTime){
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeEndTime(phoneNumber, sdrStartDateTime, sdrEndDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(serviceDetailRecordViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get ServiceDetailRecords by phone number, start time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "sdr/getServiceDetailRecordsByPhoneNumberStartTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<ServiceDetailRecordViewDTO>>> getServiceDetailRecordByPhoneNumberStartTime(@PathVariable String phoneNumber, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sdrStartDateTime){
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = framesSDRService.findServiceDetailRecordsByPhoneNumberStartTime(phoneNumber, sdrStartDateTime);
        return ResponseEntity.ok(RestAPIResponse.of(serviceDetailRecordViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get ServiceDetailRecords by phone number, Sdr code, start time, end time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "sdr/getServiceDetailRecordsByPhoneNumberSdrCodeStartTimeEndTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<ServiceDetailRecordViewDTO>>> getServiceDetailRecordByPhoneNumberSdrCodeStartTimeEndTime(@PathVariable String phoneNumber, @RequestParam String sdrCode, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sdrStartDateTime, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sdrEndDateTime){
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode(phoneNumber, sdrStartDateTime, sdrEndDateTime, sdrCode);
        return ResponseEntity.ok(RestAPIResponse.of(serviceDetailRecordViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Get ServiceDetailRecords by phone number, Sdr code, start time")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "sdr/getServiceDetailRecordsByPhoneNumberSdrCodeStartTime/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<ServiceDetailRecordViewDTO>>> getServiceDetailRecordByPhoneNumberSdrCodeStartTime(@PathVariable String phoneNumber, @RequestParam String sdrCode, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime sdrStartDateTime){
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = framesSDRService.findServiceDetailRecordsByPhoneNumberStartTimeServiceCode(phoneNumber, sdrStartDateTime, sdrCode);
        return ResponseEntity.ok(RestAPIResponse.of(serviceDetailRecordViewDTOList));
    }

    @Operation(tags = "FramesSDR Controller", description = "Delete a ServiceDetailRecord")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping(value = "/sdr/deleteServiceDetailRecord/{sdrId}", produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> deleteServiceDetailRecord(@PathVariable Long sdrId){
        framesSDRService.deleteServiceDetailRecord(sdrId);
        String message = "The Service Detail Record '" +sdrId + "' is deleted!";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

}
