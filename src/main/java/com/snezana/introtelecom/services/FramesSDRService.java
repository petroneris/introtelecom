package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface FramesSDRService {

    void saveNewPackageFrame (final PackageFrameSaveDTO packageFrameSaveDTO);

    PackageFrameViewDTO findPackageFrameById (Long packfrId);

    List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTimeEndTime (String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTime (String phoneNumber, LocalDateTime packfrStartDateTime);

    List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTimeEndTime (String packageCode, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime);

    List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTime (String packageCode, LocalDateTime packfrStartDateTime);

    void deletePackageFrame (Long packfrId);



    void saveNewAddonFame (final AddonFrameSaveDTO addonFrameSaveDTO);

    AddonFrameViewDTO findAddonFrameById (Long addfrId);

    List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeEndTime (String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime);

    List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTime (String phoneNumber, LocalDateTime addfrStartDateTime);

    List<AddonFrameViewDTO> findAddonFramesByPhoneNumberAddonCode (String phoneNumber, String addonCode);

    List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeEndTimeAddonCode (String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime, String addonCode);

    List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeAddonCode (String phoneNumber, LocalDateTime addfrStartDateTime, String addonCode);

    void deleteAddonFame (Long addfrId);



    void saveNewServiceDetailRecord (final ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO);

    void updateServiceDetailRecord (final ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO, Long sdrId);

    ServiceDetailRecordViewDTO findServiceDetailRecordById (Long sdrId);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTime (String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTime (String phoneNumber, LocalDateTime addfrStartDateTime);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberServiceCode (String phoneNumber, String serviceCode);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode (String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime, String serviceCode);

    List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeServiceCode (String phoneNumber, LocalDateTime sdrStartDateTime, String serviceCode);

    void deleteServiceDetailRecord (Long sdrId);

}
