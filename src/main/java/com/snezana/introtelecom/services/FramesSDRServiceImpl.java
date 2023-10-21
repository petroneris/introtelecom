package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.*;
import com.snezana.introtelecom.enums.PackageCodeType;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.mapper.AddonFrameMapper;
import com.snezana.introtelecom.mapper.PackageFrameMapper;
import com.snezana.introtelecom.mapper.ServiceDetailRecordMapper;
import com.snezana.introtelecom.repositories.*;
import com.snezana.introtelecom.utility.HowFarApartTwoLocalDateTime;
import com.snezana.introtelecom.utility.ResultingAmountOfService;
import com.snezana.introtelecom.utility.SdrAmountAux;
import com.snezana.introtelecom.validations.FramesSDRValidationService;
import com.snezana.introtelecom.validations.PackageAddonPhoneServValidationService;
import com.snezana.introtelecom.validations.PhoneValidationService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
@Transactional
public class FramesSDRServiceImpl implements FramesSDRService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FramesSDRServiceImpl.class);


    @Autowired
    PhoneValidationService phoneValidationService;

    @Autowired
    FramesSDRValidationService framesSDRValidationService;

    @Autowired
    PackageAddonPhoneServValidationService packageAddonPhoneServValidationService;

    @Autowired
    PackageFrameMapper packageFrameMapper;

    @Autowired
    AddonFrameMapper addonFrameMapper;

    @Autowired
    ServiceDetailRecordMapper serviceDetailRecordMapper;

    @Autowired
    PhoneRepo phoneRepo;

    @Autowired
    AddOnRepo addOnRepo;

    @Autowired
    PackageFrameRepo packageFrameRepo;

    @Autowired
    AddonFrameRepo addonFrameRepo;

    @Autowired
    PhoneServiceRepo phoneServiceRepo;

    @Autowired
    ServiceDetailRecordRepo serviceDetailRecordRepo;

    @Override
    public void saveNewPackageFrame(PackageFrameSaveDTO packageFrameSaveDTO) {
        phoneValidationService.controlThePhoneExists(packageFrameSaveDTO.getPhone());
        phoneValidationService.checkThatPhoneHasCustomersPackageCode(packageFrameSaveDTO.getPhone());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packageFrameSaveDTO.getPackfrStartDateTime());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packageFrameSaveDTO.getPackfrEndDateTime());
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(packageFrameSaveDTO.getPackfrStartDateTime(), packageFrameSaveDTO.getPackfrEndDateTime());
        framesSDRValidationService.controlTheMonthlyPackageFrameExistsAlready(packageFrameSaveDTO.getPhone(), packageFrameSaveDTO.getPackfrStartDateTime(), packageFrameSaveDTO.getPackfrEndDateTime());
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
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(packfrStartDateTime, packfrEndDateTime);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual(phoneNumber, packfrStartDateTime, packfrEndDateTime);
        List<PackageFrameViewDTO> packageFrameViewDTOList = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrameList);
        return packageFrameViewDTOList;
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTime(String phoneNumber, LocalDateTime packfrStartDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqual(phoneNumber, packfrStartDateTime);
        List<PackageFrameViewDTO> packageFrameViewDTOList = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrameList);
        return packageFrameViewDTOList;
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTimeEndTime(String packageCode, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime) {
        phoneValidationService.controlThePackageCodeExists(packageCode);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(packfrStartDateTime, packfrEndDateTime);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual(packageCode, packfrStartDateTime, packfrEndDateTime);
        List<PackageFrameViewDTO> packageFrameViewDTOList = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrameList);
        return packageFrameViewDTOList;
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTime(String packageCode, LocalDateTime packfrStartDateTime) {
        phoneValidationService.controlThePackageCodeExists(packageCode);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqual(packageCode, packfrStartDateTime);
        List<PackageFrameViewDTO> packageFrameViewDTOList = packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrameList);
        return packageFrameViewDTOList;
    }

    @Override
    public void changePackageFrameStatus(Long packfrId) {
        framesSDRValidationService.controlThePackageFrameExists(packfrId);
        PackageFrame packageFrame = packageFrameRepo.findByPackfrId(packfrId);
        if (packageFrame.getPackfrStatus().equals(StatusType.PRESENT.getStatus())){
            packageFrame.setPackfrStatus(StatusType.NOT_IN_USE.getStatus());
        } else {
            packageFrame.setPackfrStatus(StatusType.PRESENT.getStatus());
        }
        packageFrameRepo.save(packageFrame);
    }

    @Override
    public void deletePackageFrame(Long packfrId) {
        framesSDRValidationService.controlThePackageFrameExists(packfrId);
        PackageFrame packageFrame = packageFrameRepo.findByPackfrId(packfrId);
        packageFrameRepo.delete(packageFrame);
    }


    @Override
    public void saveNewAddonFame(AddonFrameSaveDTO addonFrameSaveDTO) {
        phoneValidationService.controlThePhoneExists(addonFrameSaveDTO.getPhone());
        packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonFrameSaveDTO.getAddOn());
        phoneValidationService.checkThatPhoneHasCustomersPackageCode(addonFrameSaveDTO.getPhone());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(addonFrameSaveDTO.getAddfrStartDateTime());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(addonFrameSaveDTO.getAddfrEndDateTime());
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(addonFrameSaveDTO.getAddfrStartDateTime(), addonFrameSaveDTO.getAddfrEndDateTime());
        framesSDRValidationService.controlTheAddonFrameHasAlreadyGiven(addonFrameSaveDTO.getPhone(), addonFrameSaveDTO.getAddOn(), addonFrameSaveDTO.getAddfrStartDateTime(), addonFrameSaveDTO.getAddfrEndDateTime());
        AddonFrame addonFrame = new AddonFrame();
        addonFrameMapper.addonFrameSaveDtoToAddonFrame(addonFrameSaveDTO, addonFrame, phoneRepo, addOnRepo);
        setAddonFrameByAddonCode(addonFrame);
        addonFrameRepo.save(addonFrame);
    }

    @Override
    public AddonFrameViewDTO findAddonFrameById(Long addfrId) {
        framesSDRValidationService.controlTheAddonFrameExists(addfrId);
        AddonFrame addonFrame = addonFrameRepo.findByAddfrId(addfrId);
        AddonFrameViewDTO addonFrameViewDTO = addonFrameMapper.addonFrameToAddonFrameViewDTO(addonFrame);
        return addonFrameViewDTO;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeEndTime(String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(addfrStartDateTime, addfrEndDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(phoneNumber, addfrStartDateTime, addfrEndDateTime);
        List<AddonFrameViewDTO> addonFrameViewDTOList = addonFrameMapper.addonFrameToAddonFrameViewDTO(addonFrameList);
        return addonFrameViewDTOList;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTime(String phoneNumber, LocalDateTime addfrStartDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqual(phoneNumber, addfrStartDateTime);
        List<AddonFrameViewDTO> addonFrameViewDTOList = addonFrameMapper.addonFrameToAddonFrameViewDTO(addonFrameList);
        return addonFrameViewDTOList;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByAddonCodeStartTimeEndTime(String addonCode, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime) {
        packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonCode);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(addfrStartDateTime, addfrEndDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(addonCode, addfrStartDateTime, addfrEndDateTime);
        List<AddonFrameViewDTO> addonFrameViewDTOList = addonFrameMapper.addonFrameToAddonFrameViewDTO(addonFrameList);
        return addonFrameViewDTOList;
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByAddonCodeStartTime(String addonCode, LocalDateTime addfrStartDateTime) {
        packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonCode);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqual(addonCode, addfrStartDateTime);
        List<AddonFrameViewDTO> addonFrameViewDTOList = addonFrameMapper.addonFrameToAddonFrameViewDTO(addonFrameList);
        return addonFrameViewDTOList;
    }

    @Override
    public void changeAddonFrameStatus(Long addfrId) {
        framesSDRValidationService.controlTheAddonFrameExists(addfrId);
        AddonFrame addonFrame = addonFrameRepo.findByAddfrId(addfrId);
        if (addonFrame.getAddfrStatus().equals(StatusType.PRESENT.getStatus())){
            addonFrame.setAddfrStatus(StatusType.NOT_IN_USE.getStatus());
        } else {
            addonFrame.setAddfrStatus(StatusType.PRESENT.getStatus());
        }
        addonFrameRepo.save(addonFrame);
    }

    @Override
    public void deleteAddonFrame(Long addfrId) {
        framesSDRValidationService.controlTheAddonFrameExists(addfrId);
        AddonFrame addonFrame = addonFrameRepo.findByAddfrId(addfrId);
        addonFrameRepo.delete(addonFrame);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String saveNewServiceDetailRecord(ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO) {
        String message = "Not EOS";
        phoneValidationService.controlThePhoneExists(serviceDetailRecordSaveDTO.getPhone());
        packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceDetailRecordSaveDTO.getPhoneService());
        phoneValidationService.checkThatPhoneHasCustomersPackageCode(serviceDetailRecordSaveDTO.getPhone());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(serviceDetailRecordSaveDTO.getSdrStartDateTime());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(serviceDetailRecordSaveDTO.getSdrEndDateTime());
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(serviceDetailRecordSaveDTO.getSdrStartDateTime(), serviceDetailRecordSaveDTO.getSdrEndDateTime());
        log.info("above controlTheServiceDetailRecordAlreadyExists");
        framesSDRValidationService.controlTheServiceDetailRecordAlreadyExists(serviceDetailRecordSaveDTO.getPhone(), serviceDetailRecordSaveDTO.getPhoneService(), serviceDetailRecordSaveDTO.getSdrStartDateTime(), serviceDetailRecordSaveDTO.getSdrEndDateTime());
        log.info("above phone");
        ServiceDetailRecord serviceDetailRecord = new ServiceDetailRecord();
        /////////////////////////////////////////////////////////
        Phone phone = phoneRepo.findByPhoneNumber(serviceDetailRecordSaveDTO.getPhone());
        String packageCode = phone.getPackagePlan().getPackageCode();
        String serviceCode = serviceDetailRecordSaveDTO.getPhoneService();
        String addonCode = findAddonCode(serviceCode);
        log.info("in save sdr");
        int duration = HowFarApartTwoLocalDateTime.howFarApart(serviceDetailRecordSaveDTO.getSdrStartDateTime(), serviceDetailRecordSaveDTO.getSdrEndDateTime());
        log.info("duration = " +duration);
        serviceDetailRecord.setDuration(duration);
        SdrAmountAux result = new SdrAmountAux(duration, serviceDetailRecordSaveDTO.getMsgAmount(), serviceDetailRecordSaveDTO.getMbAmount(), new BigDecimal("0.00"), "", serviceCode, false);
        if ((((packageCode.equals(PackageCodeType.PST13.getPackageCode()) || packageCode.equals(PackageCodeType.PST14.getPackageCode()))&&(serviceCode.equals("SDRCLS") || serviceCode.equals("SDRSMS"))) || ((packageCode.equals(PackageCodeType.PST14.getPackageCode()))&& serviceCode.equals("SDRINT")))) {
            log.info("not unlimited!");
        } else {
            Month currentMonth = serviceDetailRecordSaveDTO.getSdrStartDateTime().getMonth();
            int currentYear = serviceDetailRecordSaveDTO.getSdrStartDateTime().getYear();
            LocalDateTime monthlyStartDateTime = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0, 0, 0);
            LocalDateTime monthlyEndDateTime = monthlyStartDateTime.plusMonths(1);
            PackageFrame monthlyPackageFrame = packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(phone.getPhoneNumber(), monthlyStartDateTime, monthlyEndDateTime);
            List<AddonFrame> addonFrameList = addonFrameRepo.findAddonFramesByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(phone.getPhoneNumber(), addonCode, monthlyStartDateTime, monthlyEndDateTime);
            log.info("addonFrameList is size = " +addonFrameList.size());
            ResultingAmountOfService frameInput = inputAmount(monthlyPackageFrame, addonFrameList, serviceCode);
            SdrAmountAux sdrOutput = outputAmount(phone.getPhoneNumber(), monthlyStartDateTime, monthlyEndDateTime, serviceCode);
            result = checkEoSforThisPhoneService(serviceDetailRecordSaveDTO, frameInput, sdrOutput, duration);
        }
        if (result.isEos()) {
            if(!serviceCode.equals("SDRSMS")) {
                if (!(duration == result.getAuxDuration())) {
                    Duration durationMin = Duration.ofMinutes(result.getAuxDuration());
                    LocalDateTime endDateTime = serviceDetailRecordSaveDTO.getSdrStartDateTime().plus(durationMin);
                    serviceDetailRecordSaveDTO.setSdrEndDateTime(endDateTime);
                    serviceDetailRecord.setDuration(result.getAuxDuration());
                    log.info("not the same duration!");
                }
            }
            if (serviceCode.equals("SDRINT") || serviceCode.equals("SDRASM")) {
                serviceDetailRecordSaveDTO.setMbAmount(result.getAuxMBamount());
            }
            if (serviceCode.equals("SDRSMS")) {
                serviceDetailRecordSaveDTO.setMsgAmount(result.getAuxMsgAmount());
            }
            if (result.getServiceCode().equals("SDRCLS")) {
                message = "Call is interrupted by EOS";
            } else if (result.getServiceCode().equals("SDRSMS")) {
                message = "SMS is interrupted by EOS, the message could not be sent";
            } else if (result.getServiceCode().equals("SDRINT")) {
                message = "Internet service is interrupted by EOS";
            } else if (result.getServiceCode().equals("SDRASM")) {
                message = "Application or social media is interrupted by EOS";
            } else if (result.getServiceCode().equals("SDRICLCZ1") || result.getServiceCode().equals("SDRICLCZ2")) {
                message = "International call is interrupted by EOS";
            } else if (result.getServiceCode().equals("SDRRMGCZ1") || result.getServiceCode().equals("SDRRMGCZ2")) {
                message = "Roaming call is interrupted by EOS";
            }
        }
        log.info("save function");
        serviceDetailRecord.setSdrNote(result.getNote());
        serviceDetailRecordMapper.serviceDetailRecordSaveDtoToServiceDetailRecord(serviceDetailRecordSaveDTO, serviceDetailRecord, phoneRepo, phoneServiceRepo);
        serviceDetailRecordRepo.save(serviceDetailRecord);
        return message;
    }

    ///////////////////////////////////////////////////////////////////////

    @Override
    public ServiceDetailRecordViewDTO findServiceDetailRecordById(Long sdrId) {
        framesSDRValidationService.controlTheServiceDetailRecordExists(sdrId);
        ServiceDetailRecord serviceDetailRecord = serviceDetailRecordRepo.findBySdrId(sdrId);
        ServiceDetailRecordViewDTO serviceDetailRecordViewDTO = serviceDetailRecordMapper.serviceDetailRecordToServiceDetailRecordViewDTO(serviceDetailRecord);
        return serviceDetailRecordViewDTO;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTime(String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(sdrStartDateTime, sdrEndDateTime);
        List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqual(phoneNumber, sdrStartDateTime, sdrEndDateTime);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = serviceDetailRecordMapper.serviceDetailRecordToServiceDetailRecordViewDTO(serviceDetailRecordList);
        return serviceDetailRecordViewDTOList;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTime(String phoneNumber, LocalDateTime sdrStartDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqual(phoneNumber, sdrStartDateTime);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = serviceDetailRecordMapper.serviceDetailRecordToServiceDetailRecordViewDTO(serviceDetailRecordList);
        return serviceDetailRecordViewDTOList;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode(String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime, String serviceCode) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceCode);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(sdrStartDateTime, sdrEndDateTime);
        List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, sdrStartDateTime, sdrEndDateTime, serviceCode);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = serviceDetailRecordMapper.serviceDetailRecordToServiceDetailRecordViewDTO(serviceDetailRecordList);
        return serviceDetailRecordViewDTOList;
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeServiceCode(String phoneNumber, LocalDateTime sdrStartDateTime, String serviceCode) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceCode);
        List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndPhoneService_ServiceCode(phoneNumber, sdrStartDateTime, serviceCode);
        List<ServiceDetailRecordViewDTO> serviceDetailRecordViewDTOList = serviceDetailRecordMapper.serviceDetailRecordToServiceDetailRecordViewDTO(serviceDetailRecordList);
        return serviceDetailRecordViewDTOList;
    }

    @Override
    public void deleteServiceDetailRecord(Long sdrId) {
        framesSDRValidationService.controlTheServiceDetailRecordExists(sdrId);
        ServiceDetailRecord serviceDetailRecord = serviceDetailRecordRepo.findBySdrId(sdrId);
        serviceDetailRecordRepo.delete(serviceDetailRecord);
    }

    void setPackageFrameByPackageCode(PackageFrame packageFrame) {
        packageFrame.setPackfrStatus(StatusType.PRESENT.getStatus());
        String packageCode = packageFrame.getPhone().getPackagePlan().getPackageCode();
        switch (packageCode) {
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

    void setAddonFrameByAddonCode(AddonFrame addonFrame) {
        addonFrame.setAddfrStatus(StatusType.PRESENT.getStatus());
        String addonCode = addonFrame.getAddOn().getAddonCode();
        switch (addonCode) {
            case "ADDCLS":
                addonFrame.setAddfrCls(100);
                addonFrame.setAddfrSms(0);
                addonFrame.setAddfrInt(new BigDecimal("0.00"));
                addonFrame.setAddfrIcl(new BigDecimal("0.00"));
                addonFrame.setAddfrRmg(new BigDecimal("0.00"));
                addonFrame.setAddfrAsm(new BigDecimal("0.00"));
                break;
            case "ADDSMS":
                addonFrame.setAddfrCls(0);
                addonFrame.setAddfrSms(100);
                addonFrame.setAddfrInt(new BigDecimal("0.00"));
                addonFrame.setAddfrIcl(new BigDecimal("0.00"));
                addonFrame.setAddfrRmg(new BigDecimal("0.00"));
                addonFrame.setAddfrAsm(new BigDecimal("0.00"));
                break;
            case "ADDINT":
                addonFrame.setAddfrCls(0);
                addonFrame.setAddfrSms(0);
                addonFrame.setAddfrInt(new BigDecimal("5000.00"));
                addonFrame.setAddfrIcl(new BigDecimal("0.00"));
                addonFrame.setAddfrRmg(new BigDecimal("0.00"));
                addonFrame.setAddfrAsm(new BigDecimal("0.00"));
                break;
            case "ADDASM":
                addonFrame.setAddfrCls(0);
                addonFrame.setAddfrSms(0);
                addonFrame.setAddfrInt(new BigDecimal("0.00"));
                addonFrame.setAddfrIcl(new BigDecimal("0.00"));
                addonFrame.setAddfrRmg(new BigDecimal("0.00"));
                addonFrame.setAddfrAsm(new BigDecimal("5000.00"));
                break;
            case "ADDICL":
                addonFrame.setAddfrCls(0);
                addonFrame.setAddfrSms(0);
                addonFrame.setAddfrInt(new BigDecimal("0.00"));
                addonFrame.setAddfrIcl(new BigDecimal("200.00"));
                addonFrame.setAddfrRmg(new BigDecimal("0.00"));
                addonFrame.setAddfrAsm(new BigDecimal("0.00"));
                break;
            case "ADDRMG":
                addonFrame.setAddfrCls(0);
                addonFrame.setAddfrSms(0);
                addonFrame.setAddfrInt(new BigDecimal("0.00"));
                addonFrame.setAddfrIcl(new BigDecimal("0.00"));
                addonFrame.setAddfrRmg(new BigDecimal("200.00"));
                addonFrame.setAddfrAsm(new BigDecimal("0.00"));
                break;
            default:
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    String findAddonCode(String serviceCode) {
        String addonCode = "";
        switch (serviceCode) {
            case "SDRCLS":
                addonCode = "ADDCLS";
                break;
            case "SDRSMS":
                addonCode = "ADDSMS";
                break;
            case "SDRINT":
                addonCode = "ADDINT";
                break;
            case "SDRASM":
                addonCode = "ADDASM";
                break;
            case "SDRICLCZ1":
            case "SDRICLCZ2":
                addonCode = "ADDICL";
                break;
            case "SDRRMGCZ1":
            case "SDRRMGCZ2":
                addonCode = "ADDRMG";
                break;
            default:
                addonCode = "";
                break;
        }
        return addonCode;
    }

    public ResultingAmountOfService inputAmount(PackageFrame packageFrame, List<AddonFrame> addonFrameList, String serviceCode) {
        ResultingAmountOfService inputAm = new ResultingAmountOfService(0, 0, new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));
        int cls = 0;
        int sms = 0;
        BigDecimal intr = new BigDecimal("0.00");
        BigDecimal asm = new BigDecimal("0.00");
        BigDecimal icl = new BigDecimal("0.00");
        BigDecimal rmg = new BigDecimal("0.00");
        cls = cls + packageFrame.getPackfrCls();
        sms = sms + packageFrame.getPackfrSms();
        intr = intr.add(packageFrame.getPackfrInt());
        asm = asm.add(packageFrame.getPackfrAsm());
        icl = icl.add(packageFrame.getPackfrIcl());
        log.info("icl no af = " +icl);
        rmg = rmg.add(packageFrame.getPackfrRmg());
        log.info("rmg no af = " +rmg);
        if ( addonFrameList.size()>0) {
            for (AddonFrame af : addonFrameList) {
                if (serviceCode.equals("SDRCLS")) {
                    cls = cls + af.getAddfrCls();
                } else if (serviceCode.equals("SDRSMS")) {
                    sms = sms + af.getAddfrSms();
                } else if (serviceCode.equals("SDRINT")) {
                    intr = intr.add(af.getAddfrInt());
                } else if (serviceCode.equals("SDRASM")) {
                    asm = asm.add(af.getAddfrAsm());
                } else if (serviceCode.equals("SDRICLCZ1") || serviceCode.equals("SDRICLCZ2")) {
                    icl = icl.add(af.getAddfrIcl());
                    log.info("icl with af = " +icl);
                } else if (serviceCode.equals("SDRRMGCZ1") || serviceCode.equals("SDRRMGCZ2")) {
                    rmg = rmg.add(af.getAddfrRmg());
                    log.info("rmg with af = " +rmg);
                }
            }
        }
        inputAm.setAmountCls(cls);
        log.info("inputAm CLS = " +cls);
        inputAm.setAmountSms(sms);
        log.info("inputAm SMS = " +sms);
        inputAm.setAmountInt(intr);
        log.info("inputAm INT = " +intr);
        inputAm.setAmountAsm(asm);
        log.info("inputAm ASM = " +asm);
        inputAm.setAmountIcl(icl);
        log.info("inputAm ICL = " +icl);
        inputAm.setAmountRmg(rmg);
        log.info("inputAm RMG = " +rmg);
        return inputAm;
    }

    public SdrAmountAux outputAmount(String phoneNumber, LocalDateTime monthlyStartDateTime, LocalDateTime monthlyEndDateTime, String serviceCode) {
        SdrAmountAux outputAux = new SdrAmountAux(0, 0, new BigDecimal("0.00"), new BigDecimal("0.00"), "", "", false);
        int unitPriceICLCZ1 = 15;
        int unitPriceICLCZ2 = 20;
        int unitPriceRMGCZ1 = 4;
        int unitPriceRMGCZ2 = 8;
        int duration = 0;
        int duration1 = 0;
        int duration2 = 0;
        int msgAmount = 0;
        BigDecimal mbAmount = new BigDecimal("0.00");
        BigDecimal priceAmount = new BigDecimal("0.00");
        BigDecimal priceAmountICLCZ1 = new BigDecimal(unitPriceICLCZ1);
        BigDecimal priceAmountICLCZ2 = new BigDecimal(unitPriceICLCZ2);
        BigDecimal priceAmountRMGCZ1 = new BigDecimal(unitPriceRMGCZ1);
        BigDecimal priceAmountRMGCZ2 = new BigDecimal(unitPriceRMGCZ2);
        if (serviceCode.equals("SDRCLS")) {
            List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, serviceCode);
            if (serviceDetailRecordList.size()>0) {
                for (ServiceDetailRecord sdr : serviceDetailRecordList) {
                    duration = duration + sdr.getDuration();
                }
                outputAux.setAuxDuration(duration);
            }
        } else if (serviceCode.equals("SDRSMS")) {
            List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, serviceCode);
            if (serviceDetailRecordList.size()>0) {
                for (ServiceDetailRecord sdr : serviceDetailRecordList) {
                    msgAmount = msgAmount + sdr.getMsgAmount();
                }
                outputAux.setAuxMsgAmount(msgAmount);

            }
        } else if (serviceCode.equals("SDRINT") || serviceCode.equals("SDRASM")) {
            List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, serviceCode);
            if (serviceDetailRecordList.size()>0) {
                for (ServiceDetailRecord sdr : serviceDetailRecordList) {
                    mbAmount = mbAmount.add(sdr.getMbAmount());
                }
                outputAux.setAuxMBamount(mbAmount);
            }
        } else if (serviceCode.equals("SDRICLCZ1") || serviceCode.equals("SDRICLCZ2")) {
            List<ServiceDetailRecord> serviceDetailRecordList1 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, "SDRICLCZ1");
            if (serviceDetailRecordList1.size()>0) {
                for (ServiceDetailRecord sdr : serviceDetailRecordList1) {
                    duration1 = duration1 + sdr.getDuration();
                }
            }
            log.info("ICL duration1= " +duration1);
            priceAmountICLCZ1 = priceAmountICLCZ1.multiply(new BigDecimal(duration1));
            log.info("ICL priceAmountICLCZ1= " +priceAmountICLCZ1);
            List<ServiceDetailRecord> serviceDetailRecordList2 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, "SDRICLCZ2");
            if (serviceDetailRecordList2.size()>0) {
                for (ServiceDetailRecord sdr : serviceDetailRecordList2) {
                    duration2 = duration2 + sdr.getDuration();
                }
            }
            log.info("ICL duration2= " +duration2);
            priceAmountICLCZ2 = priceAmountICLCZ2.multiply(new BigDecimal(duration2));
            log.info("ICL priceAmountICLCZ2= " +priceAmountICLCZ2);
            priceAmount = priceAmount.add(priceAmountICLCZ1).add(priceAmountICLCZ2);
            log.info("ICL priceAmount= " +priceAmount);
            outputAux.setAuxPrice(priceAmount);
        } else if (serviceCode.equals("SDRRMGCZ1") || serviceCode.equals("SDRRMGCZ2")) {
            List<ServiceDetailRecord> serviceDetailRecordList1 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, "SDRRMGCZ1");
            if (serviceDetailRecordList1.size()>0) {
                for (ServiceDetailRecord sdr : serviceDetailRecordList1) {
                    duration1 = duration1 + sdr.getDuration();
                }
            }
            log.info("RMG duration1= " +duration1);
            priceAmountRMGCZ1 = priceAmountRMGCZ1.multiply(new BigDecimal(duration1));
            log.info("RMG priceAmountRMGCZ1= " +priceAmountRMGCZ1);
            List<ServiceDetailRecord> serviceDetailRecordList2 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, "SDRRMGCZ2");
            if (serviceDetailRecordList2.size()>0) {
                for (ServiceDetailRecord sdr : serviceDetailRecordList2) {
                    duration2 = duration2 + sdr.getDuration();
                }
            }
            log.info("RMG duration2= " +duration2);
            priceAmountRMGCZ2 = priceAmountRMGCZ2.multiply(new BigDecimal(duration2));
            log.info("RMG priceAmountRMGCZ2= " +priceAmountRMGCZ2);
            priceAmount = priceAmount.add(priceAmountRMGCZ1).add(priceAmountRMGCZ2);
            log.info("RMG priceAmount= " +priceAmount);
            outputAux.setAuxPrice(priceAmount);
        }
        log.info("outputAux, duration = " +outputAux.getAuxDuration() + ", msgAmount = " +outputAux.getAuxMsgAmount() + ", MBamount = " +outputAux.getAuxMBamount() + ", price = " +outputAux.getAuxPrice() + ", note = " +outputAux.getNote() + ", servoceCode = " +outputAux.getServiceCode() + ", eos = " +outputAux.isEos());
        return outputAux;
    }

    SdrAmountAux checkEoSforThisPhoneService (ServiceDetailRecordSaveDTO sdrSaveDTO, ResultingAmountOfService ras, SdrAmountAux sdrAa, int duration) {
        int unitPriceICLCZ1 = 15;
        int unitPriceICLCZ2 = 20;
        int unitPriceRMGCZ1 = 4;
        int unitPriceRMGCZ2 = 8;
        BigDecimal priceAmountICLCZ1 = new BigDecimal(unitPriceICLCZ1);
        BigDecimal priceAmountICLCZ2 = new BigDecimal(unitPriceICLCZ2);
        BigDecimal priceAmountRMGCZ1 = new BigDecimal(unitPriceRMGCZ1);
        BigDecimal priceAmountRMGCZ2 = new BigDecimal(unitPriceRMGCZ2);
        SdrAmountAux result = new SdrAmountAux(0, 0, new BigDecimal("0.00"), new BigDecimal("0.00"), "", "", false);
        String serviceCode = sdrSaveDTO.getPhoneService();
        if (serviceCode.equals("SDRCLS")) {
            log.info("info - CLS");
            if (ras.getAmountCls() < (sdrAa.getAuxDuration() + duration)) {
                duration = ras.getAmountCls() - sdrAa.getAuxDuration();
                result.setAuxDuration(duration);
                result.setNote("EOS termination");
                result.setServiceCode("SDRCLS");
                result.setEos(true);
                log.info("CLS EOS - true");
            }
        } else if (serviceCode.equals("SDRSMS")) {
            log.info("info - SMS");
            if (ras.getAmountSms() < (sdrAa.getAuxMsgAmount() + sdrSaveDTO.getMsgAmount())) {
                result.setNote("EOS termination");
                result.setAuxMsgAmount(0);
                result.setServiceCode("SDRSMS");
                result.setEos(true);
                log.info("SMS EOS - true");
            }
        } else if (serviceCode.equals("SDRINT")) {
            log.info("info - INT");
            BigDecimal pfAfMB = ras.getAmountInt();
            BigDecimal deltaMB = pfAfMB.subtract(sdrAa.getAuxMBamount());
            BigDecimal sdrMB = sdrAa.getAuxMBamount().add(sdrSaveDTO.getMbAmount());
            if (pfAfMB.compareTo(sdrMB) == -1) {
                if (duration > 1) {
                    duration = duration / 2;
                }
                result.setNote("EOS termination");
                result.setAuxMBamount(deltaMB);
                result.setAuxDuration(duration);
                result.setServiceCode("SDRINT");
                result.setEos(true);
                log.info("INT EOS - true");
            }
        } else if (serviceCode.equals("SDRASM")) {
            log.info("info - ASM");
            BigDecimal pfAfMB = ras.getAmountAsm();
            BigDecimal deltaMB = pfAfMB.subtract(sdrAa.getAuxMBamount());
            BigDecimal sdrMB = sdrAa.getAuxMBamount().add(sdrSaveDTO.getMbAmount());
            if (pfAfMB.compareTo(sdrMB) == -1) {
                if (duration > 1) {
                    duration = duration / 2;
                }
                result.setNote("EOS termination");
                result.setAuxMBamount(deltaMB);
                result.setAuxDuration(duration);
                result.setServiceCode("SDRASM");
                result.setEos(true);
                log.info("ASM EOS - true");
            }
        } else if (serviceCode.equals("SDRICLCZ1") || serviceCode.equals("SDRICLCZ2")) {
            log.info("info - ICL");
            BigDecimal pfAfPrice = ras.getAmountIcl();
            log.info("ICL pfAfPrice= " +pfAfPrice);
            BigDecimal sdrPrice1 = new BigDecimal(duration);
            if (serviceCode.equals("SDRICLCZ1")) {
                sdrPrice1 = sdrPrice1.multiply(priceAmountICLCZ1);
            } else {
                sdrPrice1 = sdrPrice1.multiply(priceAmountICLCZ2);
            }
            log.info("ICL sdrPrice1= " +sdrPrice1);
            BigDecimal sdrPrice = sdrAa.getAuxPrice().add(sdrPrice1);
            log.info("ICL sdrPrice= " +sdrPrice);
            if (pfAfPrice.compareTo(sdrPrice) == -1) {
                BigDecimal sdrDeltaPrice = pfAfPrice.subtract(sdrAa.getAuxPrice());
                log.info("ICL sdrDeltaPrice= " +sdrDeltaPrice);
                BigDecimal durationBD = new BigDecimal(1);
                durationBD = durationBD.multiply(sdrDeltaPrice);
                log.info("ICL before divide durationBD= " +durationBD);
                if (serviceCode.equals("SDRICLCZ1")) {
                    durationBD = durationBD.divide(priceAmountICLCZ1, 2, RoundingMode.UP);
                    result.setServiceCode("SDRICLCZ1");
                } else {
                    durationBD = durationBD.divide(priceAmountICLCZ2, 2, RoundingMode.UP);
                    result.setServiceCode("SDRICLCZ2");
                }
                log.info("ICL durationBD= " +durationBD);
                duration = durationBD.intValue();
                log.info("ICL duration= " +duration);
                result.setAuxDuration(duration);
                result.setNote("EOS termination");
                result.setEos(true);
                log.info("ICL EOS - true");
            }
        } else if (serviceCode.equals("SDRRMGCZ1") || serviceCode.equals("SDRRMGCZ2")) {
            log.info("info - RMG");
            BigDecimal pfAfPrice = ras.getAmountRmg();
            log.info("RMG pfAfPrice= " +pfAfPrice);
            BigDecimal sdrPrice1 = new BigDecimal(duration);
            if (serviceCode.equals("SDRRMGCZ1")) {
                sdrPrice1 = sdrPrice1.multiply(priceAmountRMGCZ1);
            } else {
                sdrPrice1 = sdrPrice1.multiply(priceAmountRMGCZ2);
            }
            log.info("RMG sdrPrice1= " +sdrPrice1);
            BigDecimal sdrPrice = sdrAa.getAuxPrice().add(sdrPrice1);
            log.info("RMG sdrPrice= " +sdrPrice);
            if (pfAfPrice.compareTo(sdrPrice) == -1) {
                BigDecimal sdrDeltaPrice = pfAfPrice.subtract(sdrAa.getAuxPrice());
                log.info("RMG sdrDeltaPrice= " +sdrDeltaPrice);
                BigDecimal durationBD = new BigDecimal(1);
                durationBD = durationBD.multiply(sdrDeltaPrice);
                log.info("RMG before divide durationBD= " +durationBD);
                if (serviceCode.equals("SDRRMGCZ1")) {
                    durationBD = durationBD.divide(priceAmountRMGCZ1, 2, RoundingMode.UP);
                    result.setServiceCode("SDRRMGCZ1");
                } else {
                    durationBD = durationBD.divide(priceAmountRMGCZ2, 2, RoundingMode.UP);
                    result.setServiceCode("SDRRMGCZ2");
                }
                log.info("RMG durationBD= " +durationBD);
                duration = durationBD.intValue();
                log.info("RMG duration= " +duration);
                result.setAuxDuration(duration);
                result.setNote("EOS termination");
                result.setEos(true);
                log.info("RMG EOS - true");
            }
        }
        log.info("result, duration = " +result.getAuxDuration() + ", msgAmount = " +result.getAuxMsgAmount() + ", MBamount = " +result.getAuxMBamount() + ", price = " +result.getAuxPrice() + ", note = " +result.getNote() + ", servoceCode = " +result.getServiceCode() + ", eos = " +result.isEos());
        return result;
    }



}
