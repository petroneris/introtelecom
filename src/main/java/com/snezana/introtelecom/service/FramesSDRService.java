package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface FramesSDRService {

    PackageFrameViewDTO findPackageFrameById (Long packfrId);

    List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTimeEndTime (String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTime (String phoneNumber, LocalDateTime packfrStartDateTime);

    List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTimeEndTime (String packageCode, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTime (String packageCode, LocalDateTime packfrStartDateTime);

    void changePackageFrameStatus (Long packfrId);

    void deletePackageFrame (Long packfrId);



    void saveNewAddonFrame (AddonFrameSaveDTO addonFrameSaveDTO);

    AddonFrameViewDTO findAddonFrameById (Long addfrId);

    List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeEndTime (String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime);

    List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTime (String phoneNumber, LocalDateTime addfrStartDateTime);

    List<AddonFrameViewDTO> findAddonFramesByAddonCodeStartTimeEndTime (String addonCode, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime);

    List<AddonFrameViewDTO> findAddonFramesByAddonCodeStartTime (String addonCode, LocalDateTime addfrStartDateTime);

    void changeAddonFrameStatus (Long addfrId);

    void deleteAddonFrame (Long addfrId);



    String saveNewServiceDetailRecord (ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO);

    ServiceDetailRecordViewDTO findServiceDetailRecordById (Long sdrId);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTime (String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTime (String phoneNumber, LocalDateTime addfrStartDateTime);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode (String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime, String serviceCode);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeServiceCode (String phoneNumber, LocalDateTime sdrStartDateTime, String serviceCode);

    void deleteServiceDetailRecord (Long sdrId);

}
