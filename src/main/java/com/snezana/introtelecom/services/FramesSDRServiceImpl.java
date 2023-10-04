package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.entity.User;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.mapper.PackageFrameMapper;
import com.snezana.introtelecom.repositories.PackageFrameRepo;
import com.snezana.introtelecom.repositories.PhoneRepo;
import com.snezana.introtelecom.validations.FramesSDRValidationService;
import com.snezana.introtelecom.validations.PhoneValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FramesSDRServiceImpl implements FramesSDRService{


    @Autowired
    PhoneValidationService phoneValidationService;

    @Autowired
    FramesSDRValidationService framesSDRValidationService;

    @Autowired
    PackageFrameMapper packageFrameMapper;

    @Autowired
    PhoneRepo phoneRepo;

    @Autowired
    PackageFrameRepo packageFrameRepo;

    @Override
    public void saveNewPackageFrame(PackageFrameSaveDTO packageFrameSaveDTO) {
        phoneValidationService.controlThePhoneExists(packageFrameSaveDTO.getPhone());
        phoneValidationService.checkThatPhoneHasCustomersPackageCode(packageFrameSaveDTO.getPhone());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packageFrameSaveDTO.getPackfrStartDateTime());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packageFrameSaveDTO.getPackfrEndDateTime());
        PackageFrame packageFrame = new PackageFrame();
        packageFrameMapper.packageFrameSaveDtoToPackageFrame(packageFrameSaveDTO, packageFrame, phoneRepo);
        setPackageFrameByPackageCode(packageFrame);
        packageFrameRepo.save(packageFrame);
    }

    @Override
    public PackageFrameViewDTO findPackageFrameById(Long packfrId) {
        framesSDRValidationService.controlThePackageFrameExists(packfrId);
        PackageFrame packageFrame = packageFrameRepo.findByPackfrId(packfrId);
        PackageFrameViewDTO packageFrameViewDTO = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrame);
        return packageFrameViewDTO;
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTimeEndTime(String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packfrStartDateTime);
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packfrEndDateTime);
        List <PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeIsLessThanEqual(phoneNumber, packfrStartDateTime, packfrEndDateTime);
        List<PackageFrameViewDTO> packageFrameViewDTOList = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrameList);
        return packageFrameViewDTOList;
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTime(String phoneNumber, LocalDateTime packfrStartDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packfrStartDateTime);
        List <PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqual(phoneNumber, packfrStartDateTime);
        List<PackageFrameViewDTO> packageFrameViewDTOList = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrameList);
        return packageFrameViewDTOList;
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTimeEndTime(String packageCode, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime) {
        phoneValidationService.controlThePackageCodeExists(packageCode);
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packfrStartDateTime);
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packfrEndDateTime);
        List <PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeIsLessThanEqual( packageCode, packfrStartDateTime, packfrEndDateTime);
        List<PackageFrameViewDTO> packageFrameViewDTOList = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrameList);
        return packageFrameViewDTOList;
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTime(String packageCode, LocalDateTime packfrStartDateTime) {
        phoneValidationService.controlThePackageCodeExists(packageCode);
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packfrStartDateTime);
        List <PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqual(packageCode, packfrStartDateTime);
        List<PackageFrameViewDTO> packageFrameViewDTOList = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrameList);
        return packageFrameViewDTOList;
    }

    @Override
    public void deletePackageFrame(Long packfrId) {
        framesSDRValidationService.controlThePackageFrameExists(packfrId);
        PackageFrame packageFrame = packageFrameRepo.findByPackfrId(packfrId);
        packageFrameRepo.delete(packageFrame);
    }


    @Override
    public void saveNewAddonFame(AddonFrameSaveDTO addonFrameSaveDTO) {

    }

    @Override
    public AddonFrameViewDTO findAddonFrameById(Long addfrId) {
        return null;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeEndTime(String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime) {
        return null;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTime(String phoneNumber, LocalDateTime addfrStartDateTime) {
        return null;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberAddonCode(String phoneNumber, String addonCode) {
        return null;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeEndTimeAddonCode(String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime, String addonCode) {
        return null;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeAddonCode(String phoneNumber, LocalDateTime addfrStartDateTime, String addonCode) {
        return null;
    }

    @Override
    public void deleteAddonFame(Long addfrId) {

    }

    @Override
    public void saveNewServiceDetailRecord(ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO) {

    }

    @Override
    public void updateServiceDetailRecord(ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO, Long sdrId) {

    }

    @Override
    public ServiceDetailRecordViewDTO findServiceDetailRecordById(Long sdrId) {
        return null;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTime(String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime) {
        return null;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTime(String phoneNumber, LocalDateTime addfrStartDateTime) {
        return null;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberServiceCode(String phoneNumber, String serviceCode) {
        return null;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode(String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime, String serviceCode) {
        return null;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeServiceCode(String phoneNumber, LocalDateTime sdrStartDateTime, String serviceCode) {
        return null;
    }

    @Override
    public void deleteServiceDetailRecord(Long sdrId) {

    }

    void setPackageFrameByPackageCode(PackageFrame packageFrame){
        packageFrame.setPackfrStatus(StatusType.ACTIVE.getStatus());
        String packageCode = packageFrame.getPhone().getPackagePlan().getPackageCode();
        switch (packageCode){
            case "01":
                packageFrame.setPackfrCls(200);
                packageFrame.setPackfrSms(200);
                packageFrame.setPackfrInt(new BigDecimal("0.00"));
                packageFrame.setPackfrIcl(new BigDecimal("0.00"));
                packageFrame.setPackfrRmg(new BigDecimal("0.00"));
                packageFrame.setPackfrAsm(new BigDecimal("0.00"));
                break;
            case "02":
                packageFrame.setPackfrCls(200);
                packageFrame.setPackfrSms(200);
                packageFrame.setPackfrInt(new BigDecimal("10000.00"));
                packageFrame.setPackfrIcl(new BigDecimal("0.00"));
                packageFrame.setPackfrRmg(new BigDecimal("0.00"));
                packageFrame.setPackfrAsm(new BigDecimal("0.00"));
                break;
            case "11":
                packageFrame.setPackfrCls(300);
                packageFrame.setPackfrSms(300);
                packageFrame.setPackfrInt(new BigDecimal("10000.00"));
                packageFrame.setPackfrIcl(new BigDecimal("200.00"));
                packageFrame.setPackfrRmg(new BigDecimal("200.00"));
                packageFrame.setPackfrAsm(new BigDecimal("0.00"));
                break;
            case "12":
                packageFrame.setPackfrCls(400);
                packageFrame.setPackfrSms(400);
                packageFrame.setPackfrInt(new BigDecimal("10000.00"));
                packageFrame.setPackfrIcl(new BigDecimal("200.00"));
                packageFrame.setPackfrRmg(new BigDecimal("200.00"));
                packageFrame.setPackfrAsm(new BigDecimal("5000.00"));
                break;
            case "13":
                packageFrame.setPackfrCls(-1);
                packageFrame.setPackfrSms(-1);
                packageFrame.setPackfrInt(new BigDecimal("15000.00"));
                packageFrame.setPackfrIcl(new BigDecimal("200.00"));
                packageFrame.setPackfrRmg(new BigDecimal("200.00"));
                packageFrame.setPackfrAsm(new BigDecimal("5000.00"));
                break;
            case "14":
                packageFrame.setPackfrCls(-1);
                packageFrame.setPackfrSms(-1);
                packageFrame.setPackfrInt(new BigDecimal("-1.00"));
                packageFrame.setPackfrIcl(new BigDecimal("200.00"));
                packageFrame.setPackfrRmg(new BigDecimal("200.00"));
                packageFrame.setPackfrAsm(new BigDecimal("10000.00"));
                break;
            default:
        }
    }

}
