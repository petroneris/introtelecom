package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.entity.*;
import com.snezana.introtelecom.enums.AddonCode;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.enums.SDRCode;
import com.snezana.introtelecom.enums.StatusType;
import com.snezana.introtelecom.mapper.AddonFrameMapper;
import com.snezana.introtelecom.mapper.PackageFrameMapper;
import com.snezana.introtelecom.mapper.ServiceDetailRecordMapper;
import com.snezana.introtelecom.repositories.*;
import com.snezana.introtelecom.utils.SomeUtils;
import com.snezana.introtelecom.validations.FramesSDRValidationService;
import com.snezana.introtelecom.validations.PackageAddonPhoneServValidationService;
import com.snezana.introtelecom.validations.PhoneValidationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
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
@RequiredArgsConstructor
public class FramesSDRServiceImpl implements FramesSDRService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(FramesSDRServiceImpl.class);

    public static final int UNIT_PRICE_ICLCZ1 = 15;
    public static final int UNIT_PRICE_ICLCZ2 = 20;
    public static final int UNIT_PRICE_RMGCZ1 = 4;
    public static final int UNIT_PRICE_RMGCZ2 = 8;

    private final PhoneValidationService phoneValidationService;
    private final FramesSDRValidationService framesSDRValidationService;
    private final PackageAddonPhoneServValidationService packageAddonPhoneServValidationService;
    private final PackageFrameMapper packageFrameMapper;
    private final AddonFrameMapper addonFrameMapper;
    private final ServiceDetailRecordMapper serviceDetailRecordMapper;
    private final PhoneRepo phoneRepo;
    private final AddOnRepo addOnRepo;
    private final PackageFrameRepo packageFrameRepo;
    private final AddonFrameRepo addonFrameRepo;
    private final PhoneServiceRepo phoneServiceRepo;
    private final ServiceDetailRecordRepo serviceDetailRecordRepo;

    @Override
    public void saveNewPackageFrame(PackageFrameSaveDTO packageFrameSaveDTO) {
        Phone phone = phoneValidationService.returnThePhoneIfExists(packageFrameSaveDTO.getPhoneNumber());
        phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packageFrameSaveDTO.getPackfrStartDateTime());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(packageFrameSaveDTO.getPackfrEndDateTime());
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(packageFrameSaveDTO.getPackfrStartDateTime(), packageFrameSaveDTO.getPackfrEndDateTime());
        framesSDRValidationService.controlTheMonthlyPackageFrameAlreadyExists(packageFrameSaveDTO.getPhoneNumber(), packageFrameSaveDTO.getPackfrStartDateTime(), packageFrameSaveDTO.getPackfrEndDateTime());
        PackageFrame packageFrame = packageFrameMapper.packageFrameSaveDtoToPackageFrame(packageFrameSaveDTO, phoneRepo);
        packageFrameRepo.save(packageFrame);
    }

    @Override
    public PackageFrameViewDTO findPackageFrameById(Long packfrId) {
        PackageFrame packageFrame = framesSDRValidationService.returnThePackageFrameIfExists(packfrId);
        return packageFrameMapper.packageFrameToPackageFrameViewDTO(packageFrame);
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTimeEndTime(String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(packfrStartDateTime, packfrEndDateTime);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual(phoneNumber, packfrStartDateTime, packfrEndDateTime);
        return packageFrameMapper.packageFramesToPackageFramesViewDTO(packageFrameList);
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPhoneNumberStartTime(String phoneNumber, LocalDateTime packfrStartDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqual(phoneNumber, packfrStartDateTime);
        return packageFrameMapper.packageFramesToPackageFramesViewDTO(packageFrameList);
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTimeEndTime(String packageCode, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime) {
        packageAddonPhoneServValidationService.controlThePackageCodeExists(packageCode);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(packfrStartDateTime, packfrEndDateTime);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual(packageCode, packfrStartDateTime, packfrEndDateTime);
        return packageFrameMapper.packageFramesToPackageFramesViewDTO(packageFrameList);
    }

    @Override
    public List<PackageFrameViewDTO> findPackageFramesByPackageCodeStartTime(String packageCode, LocalDateTime packfrStartDateTime) {
        packageAddonPhoneServValidationService.controlThePackageCodeExists(packageCode);
        List<PackageFrame> packageFrameList = packageFrameRepo.findByPhone_PackagePlan_PackageCodeAndPackfrStartDateTimeGreaterThanEqual(packageCode, packfrStartDateTime);
        return packageFrameMapper.packageFramesToPackageFramesViewDTO(packageFrameList);
    }

    @Override
    public void changePackageFrameStatus(Long packfrId) {
        PackageFrame packageFrame = framesSDRValidationService.returnThePackageFrameIfExists(packfrId);
        if (packageFrame.getPackfrStatus().equals(StatusType.PRESENT.getStatus())){
            packageFrame.setPackfrStatus(StatusType.NOT_IN_USE.getStatus());
        } else {
            packageFrame.setPackfrStatus(StatusType.PRESENT.getStatus());
        }
        packageFrameRepo.save(packageFrame);
    }

    @Override
    public void deletePackageFrame(Long packfrId) {
        PackageFrame packageFrame = framesSDRValidationService.returnThePackageFrameIfExists(packfrId);
        packageFrameRepo.delete(packageFrame);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void saveNewAddonFame(AddonFrameSaveDTO addonFrameSaveDTO) {
        Phone phone = phoneValidationService.returnThePhoneIfExists(addonFrameSaveDTO.getPhoneNumber());
        phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
        packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonFrameSaveDTO.getAddonCode());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(addonFrameSaveDTO.getAddfrStartDateTime());
        framesSDRValidationService.controlTheLocalDateTimeInputIsValid(addonFrameSaveDTO.getAddfrEndDateTime());
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(addonFrameSaveDTO.getAddfrStartDateTime(), addonFrameSaveDTO.getAddfrEndDateTime());
        framesSDRValidationService.controlTheAddonFrameHasAlreadyGiven(addonFrameSaveDTO.getPhoneNumber(), addonFrameSaveDTO.getAddonCode(), addonFrameSaveDTO.getAddfrStartDateTime(), addonFrameSaveDTO.getAddfrEndDateTime());
        AddonFrame addonFrame = addonFrameMapper.addonFrameSaveDtoToAddonFrame(addonFrameSaveDTO, phoneRepo, addOnRepo);
        addonFrameRepo.save(addonFrame);
    }

    @Override
    public AddonFrameViewDTO findAddonFrameById(Long addfrId) {
        AddonFrame addonFrame = framesSDRValidationService.returnTheAddonFrameIfExists(addfrId);
        return addonFrameMapper.addonFrameToAddonFrameViewDTO(addonFrame);
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTimeEndTime(String phoneNumber, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(addfrStartDateTime, addfrEndDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(phoneNumber, addfrStartDateTime, addfrEndDateTime);
        return addonFrameMapper.addonFramesToAddonFramesViewDTO(addonFrameList);
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByPhoneNumberStartTime(String phoneNumber, LocalDateTime addfrStartDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByPhone_PhoneNumberAndAddfrStartDateTimeGreaterThanEqual(phoneNumber, addfrStartDateTime);
        return addonFrameMapper.addonFramesToAddonFramesViewDTO(addonFrameList);
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByAddonCodeStartTimeEndTime(String addonCode, LocalDateTime addfrStartDateTime, LocalDateTime addfrEndDateTime) {
        packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonCode);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(addfrStartDateTime, addfrEndDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(addonCode, addfrStartDateTime, addfrEndDateTime);
        return addonFrameMapper.addonFramesToAddonFramesViewDTO(addonFrameList);
    }

    @Override
    public List<AddonFrameViewDTO> findAddonFramesByAddonCodeStartTime(String addonCode, LocalDateTime addfrStartDateTime) {
        packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonCode);
        List<AddonFrame> addonFrameList = addonFrameRepo.findByAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqual(addonCode, addfrStartDateTime);
        return addonFrameMapper.addonFramesToAddonFramesViewDTO(addonFrameList);
    }

    @Override
    public void changeAddonFrameStatus(Long addfrId) {
        AddonFrame addonFrame = framesSDRValidationService.returnTheAddonFrameIfExists(addfrId);
        if (addonFrame.getAddfrStatus().equals(StatusType.PRESENT.getStatus())){
            addonFrame.setAddfrStatus(StatusType.NOT_IN_USE.getStatus());
        } else {
            addonFrame.setAddfrStatus(StatusType.PRESENT.getStatus());
        }
        addonFrameRepo.save(addonFrame);
    }

    @Override
    public void deleteAddonFrame(Long addfrId) {
        AddonFrame addonFrame = framesSDRValidationService.returnTheAddonFrameIfExists(addfrId);
        addonFrameRepo.delete(addonFrame);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Override
public String saveNewServiceDetailRecord(ServiceDetailRecordSaveDTO serviceDetailRecordSaveDTO) {
    String message = "Not EOS";
    Phone phone = phoneValidationService.returnThePhoneIfExists(serviceDetailRecordSaveDTO.getPhoneNumber());
    phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
    packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceDetailRecordSaveDTO.getServiceCode());
    framesSDRValidationService.controlTheLocalDateTimeInputIsValid(serviceDetailRecordSaveDTO.getSdrStartDateTime());
    framesSDRValidationService.controlTheLocalDateTimeInputIsValid(serviceDetailRecordSaveDTO.getSdrEndDateTime());
    framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(serviceDetailRecordSaveDTO.getSdrStartDateTime(), serviceDetailRecordSaveDTO.getSdrEndDateTime());
    log.info("above controlTheServiceDetailRecordAlreadyExists");
    framesSDRValidationService.controlTheServiceDetailRecordAlreadyExists(serviceDetailRecordSaveDTO.getPhoneNumber(), serviceDetailRecordSaveDTO.getServiceCode(), serviceDetailRecordSaveDTO.getSdrStartDateTime(), serviceDetailRecordSaveDTO.getSdrEndDateTime());
    log.info("above phone");
    ServiceDetailRecord serviceDetailRecord = new ServiceDetailRecord();
    /////////////////////////////////////////////////////////
    String packageCode = phone.getPackagePlan().getPackageCode();
    PackagePlanType packagePlanType = PackagePlanType.findByKey(packageCode);
    String serviceCodeStr = serviceDetailRecordSaveDTO.getServiceCode();
    log.info("serviceCode = " + serviceCodeStr);
    SDRCode serviceCode = SDRCode.valueOf(serviceCodeStr);
    AddonCode addonCode = findAddonCodeFromServiceCode(serviceCode);
    log.info("addonCode = " + addonCode);
    log.info("in save sdr");
    int duration = SomeUtils.howFarApartTwoLocalDateTime(serviceDetailRecordSaveDTO.getSdrStartDateTime(), serviceDetailRecordSaveDTO.getSdrEndDateTime());
    log.info("duration = " + duration);
    serviceDetailRecord.setDuration(duration);
    SdrAmountCalc result = new SdrAmountCalc(duration, serviceDetailRecordSaveDTO.getMsgAmount(), serviceDetailRecordSaveDTO.getMbAmount(), new BigDecimal("0.00"), "", serviceCodeStr, false);
    if (((packagePlanType == PackagePlanType.PST13 || packagePlanType == PackagePlanType.PST14) && (serviceCode == SDRCode.SDRCLS || serviceCode == SDRCode.SDRSMS)) || (packagePlanType == PackagePlanType.PST14 && serviceCode == SDRCode.SDRINT)) {
        log.info("unlimited!");
    } else {
        Month currentMonth = serviceDetailRecordSaveDTO.getSdrStartDateTime().getMonth();
        int currentYear = serviceDetailRecordSaveDTO.getSdrStartDateTime().getYear();
        LocalDateTime monthlyStartDateTime = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0, 0, 0);
        LocalDateTime monthlyEndDateTime = monthlyStartDateTime.plusMonths(1);
        PackageFrame monthlyPackageFrame = packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(phone.getPhoneNumber(), monthlyStartDateTime, monthlyEndDateTime);
        List<AddonFrame> addonFrameList = addonFrameRepo.findAddonFramesByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(phone.getPhoneNumber(), addonCode.name(), monthlyStartDateTime, monthlyEndDateTime);
        log.info("addonFrameList is size = " + addonFrameList.size());
        FramesInputTotal frameInput = inputAmountOfFrames(monthlyPackageFrame, addonFrameList, serviceCode);
        SdrAmountCalc sdrOutput = outputAmountOfSDRs(phone.getPhoneNumber(), monthlyStartDateTime, monthlyEndDateTime, serviceCode);
        result = checkEOSforThisSDR(serviceDetailRecordSaveDTO, frameInput, sdrOutput, duration);
    }
    if (result.isSdrEOS()) {
        if (!(serviceCode == SDRCode.SDRSMS)) {
            if (!(duration == result.getSdrDurationAmount())) {
                Duration durationMin = Duration.ofMinutes(result.getSdrDurationAmount());
                LocalDateTime endDateTime = serviceDetailRecordSaveDTO.getSdrStartDateTime().plus(durationMin);
                serviceDetailRecordSaveDTO.setSdrEndDateTime(endDateTime);
                serviceDetailRecord.setDuration(result.getSdrDurationAmount());
                log.info("not the same duration!");
            }
        }
        if (serviceCode == SDRCode.SDRINT || serviceCode == SDRCode.SDRASM) {
            serviceDetailRecordSaveDTO.setMbAmount(result.getSdrMBamount());
        }
        if (serviceCode == SDRCode.SDRSMS) {
            serviceDetailRecordSaveDTO.setMsgAmount(result.getSdrMsgAmount());
        }
        SDRCode resultSdrCode = SDRCode.valueOf(result.getServiceCode());
        switch (resultSdrCode) {
            case SDRCLS:
                message = "Call is interrupted by EOS";
                break;
            case SDRSMS:
                message = "SMS is interrupted by EOS, the message could not be sent";
                break;
            case SDRINT:
                message = "Internet service is interrupted by EOS";
                break;
            case SDRASM:
                message = "Application or social media is interrupted by EOS";
                break;
            case SDRICLCZ1:
            case SDRICLCZ2:
                message = "International call is interrupted by EOS";
                break;
            case SDRRMGCZ1:
            case SDRRMGCZ2:
                message = "Roaming call is interrupted by EOS";
                break;
        }
    }
    log.info("save function");
    serviceDetailRecord.setSdrNote(result.getEosNote());
    serviceDetailRecordMapper.serviceDetailRecordSaveDtoToServiceDetailRecord(serviceDetailRecordSaveDTO, serviceDetailRecord, phoneRepo, phoneServiceRepo);
    serviceDetailRecordRepo.save(serviceDetailRecord);
    return message;
}

    ///////////////////////////////////////////////////////////////////////

    @Override
    public ServiceDetailRecordViewDTO findServiceDetailRecordById(Long sdrId) {
        ServiceDetailRecord serviceDetailRecord = framesSDRValidationService.returnTheServiceDetailRecordIfExists(sdrId);
        return serviceDetailRecordMapper.serviceDetailRecordToServiceDetailRecordViewDTO(serviceDetailRecord);
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTime(String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(sdrStartDateTime, sdrEndDateTime);
        List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqual(phoneNumber, sdrStartDateTime, sdrEndDateTime);
        return serviceDetailRecordMapper.serviceDetailRecordsToServiceDetailRecordsViewDTO(serviceDetailRecordList);
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTime(String phoneNumber, LocalDateTime sdrStartDateTime) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqual(phoneNumber, sdrStartDateTime);
        return serviceDetailRecordMapper.serviceDetailRecordsToServiceDetailRecordsViewDTO(serviceDetailRecordList);
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeEndTimeServiceCode(String phoneNumber, LocalDateTime sdrStartDateTime, LocalDateTime sdrEndDateTime, String serviceCode) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceCode);
        framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(sdrStartDateTime, sdrEndDateTime);
        List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, sdrStartDateTime, sdrEndDateTime, serviceCode);
        return serviceDetailRecordMapper.serviceDetailRecordsToServiceDetailRecordsViewDTO(serviceDetailRecordList);
    }

    @Override
    public List<ServiceDetailRecordViewDTO> findServiceDetailRecordsByPhoneNumberStartTimeServiceCode(String phoneNumber, LocalDateTime sdrStartDateTime, String serviceCode) {
        phoneValidationService.controlThePhoneExists(phoneNumber);
        packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceCode);
        List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndPhoneService_ServiceCode(phoneNumber, sdrStartDateTime, serviceCode);
        return serviceDetailRecordMapper.serviceDetailRecordsToServiceDetailRecordsViewDTO(serviceDetailRecordList);
    }

    @Override
    public void deleteServiceDetailRecord(Long sdrId) {
        ServiceDetailRecord serviceDetailRecord = framesSDRValidationService.returnTheServiceDetailRecordIfExists(sdrId);
        serviceDetailRecordRepo.delete(serviceDetailRecord);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    AddonCode findAddonCodeFromServiceCode(SDRCode sdrCode) {
        AddonCode addonCode;
        switch (sdrCode) {
            case SDRCLS:
                addonCode = AddonCode.ADDCLS;
                break;
            case SDRSMS:
                addonCode = AddonCode.ADDSMS;
                break;
            case SDRINT:
                addonCode = AddonCode.ADDINT;
                break;
            case SDRASM:
                addonCode = AddonCode.ADDASM;
                break;
            case SDRICLCZ1:
            case SDRICLCZ2:
                addonCode = AddonCode.ADDICL;
                break;
            case SDRRMGCZ1:
            case SDRRMGCZ2:
                addonCode = AddonCode.ADDRMG;
                break;
            default:
                addonCode = AddonCode.ADDNULL;
                break;
        }
        return addonCode;
    }

    FramesInputTotal inputAmountOfFrames(PackageFrame packageFrame, List<AddonFrame> addonFrameList, SDRCode serviceCode) {
        FramesInputTotal inputResult = new FramesInputTotal(0, 0, new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"), new BigDecimal("0.00"));
        int inputCls = packageFrame.getPackfrCls();
        int inputSms = packageFrame.getPackfrSms();
        BigDecimal inputInt = packageFrame.getPackfrInt();
        BigDecimal inputAsm = packageFrame.getPackfrAsm();
        BigDecimal inputIcl = packageFrame.getPackfrIcl();
        log.info("icl no af = " + inputIcl);
        BigDecimal inputRmg = packageFrame.getPackfrRmg();
        log.info("rmg no af = " + inputRmg);
        if (!addonFrameList.isEmpty()) {
            for (AddonFrame af : addonFrameList) {
                switch (serviceCode) {
                    case SDRCLS:
                        inputCls = inputCls + af.getAddfrCls();
                        break;
                    case SDRSMS:
                        inputSms = inputSms + af.getAddfrSms();
                        break;
                    case SDRINT:
                        inputInt = inputInt.add(af.getAddfrInt());
                        break;
                    case SDRASM:
                        inputAsm = inputAsm.add(af.getAddfrAsm());
                        break;
                    case SDRICLCZ1:
                    case SDRICLCZ2:
                        inputIcl = inputIcl.add(af.getAddfrIcl());
                        log.info("icl with af = " + inputIcl);
                        break;
                    case SDRRMGCZ1:
                    case SDRRMGCZ2:
                        inputRmg = inputRmg.add(af.getAddfrRmg());
                        log.info("rmg with af = " + inputRmg);
                        break;
                }
            }
        }
        inputResult.setInputCls(inputCls);
        log.info("inputAm CLS = " + inputCls);
        inputResult.setInputSms(inputSms);
        log.info("inputAm SMS = " + inputSms);
        inputResult.setInputInt(inputInt);
        log.info("inputAm INT = " + inputInt);
        inputResult.setInputAsm(inputAsm);
        log.info("inputAm ASM = " + inputAsm);
        inputResult.setInputIcl(inputIcl);
        log.info("inputAm ICL = " + inputIcl);
        inputResult.setInputRmg(inputRmg);
        log.info("inputAm RMG = " + inputRmg);
        return inputResult;
    }

    SdrAmountCalc outputAmountOfSDRs(String phoneNumber, LocalDateTime monthlyStartDateTime, LocalDateTime monthlyEndDateTime, SDRCode serviceCode) {
        SdrAmountCalc outputResult = new SdrAmountCalc(0, 0, new BigDecimal("0.00"), new BigDecimal("0.00"), "", "", false);
        int duration = 0;
        int durationCZ1 = 0;
        int durationCZ2 = 0;
        int msgAmount = 0;
        BigDecimal mbAmount = new BigDecimal("0.00");
        BigDecimal priceAmount = new BigDecimal("0.00");
        BigDecimal priceICLCZ1 = new BigDecimal(UNIT_PRICE_ICLCZ1);
        BigDecimal priceICLCZ2 = new BigDecimal(UNIT_PRICE_ICLCZ2);
        BigDecimal priceRMGCZ1 = new BigDecimal(UNIT_PRICE_RMGCZ1);
        BigDecimal priceRMGCZ2 = new BigDecimal(UNIT_PRICE_RMGCZ2);
        switch (serviceCode) {
            case SDRCLS: {
                List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, serviceCode.name());
                if (!serviceDetailRecordList.isEmpty()) {
                    for (ServiceDetailRecord sdr : serviceDetailRecordList) {
                        duration = duration + sdr.getDuration();
                    }
                    outputResult.setSdrDurationAmount(duration);
                }
                break;
            }
            case SDRSMS: {
                List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, serviceCode.name());
                if (!serviceDetailRecordList.isEmpty()) {
                    for (ServiceDetailRecord sdr : serviceDetailRecordList) {
                        msgAmount = msgAmount + sdr.getMsgAmount();
                    }
                    outputResult.setSdrMsgAmount(msgAmount);
                }
                break;
            }
            case SDRINT:
            case SDRASM: {
                List<ServiceDetailRecord> serviceDetailRecordList = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, serviceCode.name());
                if (!serviceDetailRecordList.isEmpty()) {
                    for (ServiceDetailRecord sdr : serviceDetailRecordList) {
                        mbAmount = mbAmount.add(sdr.getMbAmount());
                    }
                    outputResult.setSdrMBamount(mbAmount);
                }
                break;
            }
            case SDRICLCZ1:
            case SDRICLCZ2: {
                List<ServiceDetailRecord> serviceDetailRecordList1 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, SDRCode.SDRICLCZ1.name());
                if (!serviceDetailRecordList1.isEmpty()) {
                    for (ServiceDetailRecord sdr : serviceDetailRecordList1) {
                        durationCZ1 = durationCZ1 + sdr.getDuration();
                    }
                }
                log.info("ICL duration1= " + durationCZ1);
                priceICLCZ1 = priceICLCZ1.multiply(new BigDecimal(durationCZ1));
                log.info("ICL priceAmountICLCZ1= " + priceICLCZ1);
                List<ServiceDetailRecord> serviceDetailRecordList2 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, SDRCode.SDRICLCZ2.name());
                if (!serviceDetailRecordList2.isEmpty()) {
                    for (ServiceDetailRecord sdr : serviceDetailRecordList2) {
                        durationCZ2 = durationCZ2 + sdr.getDuration();
                    }
                }
                log.info("ICL duration2= " + durationCZ2);
                priceICLCZ2 = priceICLCZ2.multiply(new BigDecimal(durationCZ2));
                log.info("ICL priceAmountICLCZ2= " + priceICLCZ2);
                priceAmount = priceAmount.add(priceICLCZ1).add(priceICLCZ2);
                log.info("ICL priceAmount= " + priceAmount);
                outputResult.setSdrPriceAmount(priceAmount);
                break;
            }
            case SDRRMGCZ1:
            case SDRRMGCZ2: {
                List<ServiceDetailRecord> serviceDetailRecordList1 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, SDRCode.SDRRMGCZ1.name());
                if (!serviceDetailRecordList1.isEmpty()) {
                    for (ServiceDetailRecord sdr : serviceDetailRecordList1) {
                        durationCZ1 = durationCZ1 + sdr.getDuration();
                    }
                }
                log.info("RMG duration1= " + durationCZ1);
                priceRMGCZ1 = priceRMGCZ1.multiply(new BigDecimal(durationCZ1));
                log.info("RMG priceAmountRMGCZ1= " + priceRMGCZ1);
                List<ServiceDetailRecord> serviceDetailRecordList2 = serviceDetailRecordRepo.findByPhone_PhoneNumberAndSdrStartDateTimeGreaterThanEqualAndSdrEndDateTimeLessThanEqualAndPhoneService_ServiceCode(phoneNumber, monthlyStartDateTime, monthlyEndDateTime, SDRCode.SDRRMGCZ2.name());
                if (!serviceDetailRecordList2.isEmpty()) {
                    for (ServiceDetailRecord sdr : serviceDetailRecordList2) {
                        durationCZ2 = durationCZ2 + sdr.getDuration();
                    }
                }
                log.info("RMG duration2= " + durationCZ2);
                priceRMGCZ2 = priceRMGCZ2.multiply(new BigDecimal(durationCZ2));
                log.info("RMG priceAmountRMGCZ2= " + priceRMGCZ2);
                priceAmount = priceAmount.add(priceRMGCZ1).add(priceRMGCZ2);
                log.info("RMG priceAmount= " + priceAmount);
                outputResult.setSdrPriceAmount(priceAmount);
                break;
            }
        }
        log.info("outputResult, duration = " + outputResult.getSdrDurationAmount() + ", msgAmount = " + outputResult.getSdrMsgAmount() + ", MBamount = " + outputResult.getSdrMBamount() + ", price = " + outputResult.getSdrPriceAmount() + ", note = " + outputResult.getEosNote() + ", serviceCode = " + outputResult.getServiceCode() + ", eos = " + outputResult.isSdrEOS());
        return outputResult;
    }

    SdrAmountCalc checkEOSforThisSDR(ServiceDetailRecordSaveDTO sdrSaveDTO, FramesInputTotal framesInput, SdrAmountCalc sdrOutput, int duration) {
        BigDecimal priceICLCZ1 = new BigDecimal(UNIT_PRICE_ICLCZ1);
        BigDecimal priceICLCZ2 = new BigDecimal(UNIT_PRICE_ICLCZ2);
        BigDecimal priceRMGCZ1 = new BigDecimal(UNIT_PRICE_RMGCZ1);
        BigDecimal priceRMGCZ2 = new BigDecimal(UNIT_PRICE_RMGCZ2);
        SdrAmountCalc result = new SdrAmountCalc(0, 0, new BigDecimal("0.00"), new BigDecimal("0.00"), "", "", false);
        String serviceCodeStr = sdrSaveDTO.getServiceCode();
        SDRCode serviceCode = SDRCode.valueOf(serviceCodeStr);
        switch (serviceCode) {
            case SDRCLS:
                log.info("info - CLS");
                if (framesInput.getInputCls() < (sdrOutput.getSdrDurationAmount() + duration)) {
                    duration = framesInput.getInputCls() - sdrOutput.getSdrDurationAmount();
                    result.setSdrDurationAmount(duration);
                    result.setEosNote("EOS termination");
                    result.setServiceCode(SDRCode.SDRCLS.name());
                    result.setSdrEOS(true);
                    log.info("CLS EOS - true");
                }
                break;
            case SDRSMS:
                log.info("info - SMS");
                if (framesInput.getInputSms() < (sdrOutput.getSdrMsgAmount() + sdrSaveDTO.getMsgAmount())) {
                    result.setEosNote("EOS termination");
                    result.setSdrMsgAmount(0);
                    result.setServiceCode(SDRCode.SDRSMS.name());
                    result.setSdrEOS(true);
                    log.info("SMS EOS - true");
                }
                break;
            case SDRINT: {
                log.info("info - INT");
                BigDecimal inputMB = framesInput.getInputInt();
                BigDecimal deltaMB = inputMB.subtract(sdrOutput.getSdrMBamount());
                BigDecimal outputMB = sdrOutput.getSdrMBamount().add(sdrSaveDTO.getMbAmount());
                if (inputMB.compareTo(outputMB) < 0) {
                    if (duration > 1) {
                        duration = duration / 2;
                    }
                    result.setEosNote("EOS termination");
                    result.setSdrMBamount(deltaMB);
                    result.setSdrDurationAmount(duration);
                    result.setServiceCode(SDRCode.SDRINT.name());
                    result.setSdrEOS(true);
                    log.info("INT EOS - true");
                }
                break;
            }
            case SDRASM: {
                log.info("info - ASM");
                BigDecimal inputMB = framesInput.getInputAsm();
                BigDecimal deltaMB = inputMB.subtract(sdrOutput.getSdrMBamount());
                BigDecimal outputMB = sdrOutput.getSdrMBamount().add(sdrSaveDTO.getMbAmount());
                if (inputMB.compareTo(outputMB) < 0) {
                    if (duration > 1) {
                        duration = duration / 2;
                    }
                    result.setEosNote("EOS termination");
                    result.setSdrMBamount(deltaMB);
                    result.setSdrDurationAmount(duration);
                    result.setServiceCode(SDRCode.SDRASM.name());
                    result.setSdrEOS(true);
                    log.info("ASM EOS - true");
                }
                break;
            }
            case SDRICLCZ1:
            case SDRICLCZ2: {
                log.info("info - ICL");
                BigDecimal inputPrice = framesInput.getInputIcl();
                log.info("ICL pfAfPrice= " + inputPrice);
                BigDecimal thisSdrPrice = new BigDecimal(duration);
                if (serviceCode.equals(SDRCode.SDRICLCZ1)) {
                    thisSdrPrice = thisSdrPrice.multiply(priceICLCZ1);
                } else {
                    thisSdrPrice = thisSdrPrice.multiply(priceICLCZ2);
                }
                log.info("ICL sdrPrice1= " + thisSdrPrice);
                BigDecimal outputPrice = sdrOutput.getSdrPriceAmount().add(thisSdrPrice);
                log.info("ICL sdrPrice= " + outputPrice);
                if (inputPrice.compareTo(outputPrice) < 0) {
                    BigDecimal deltaPrice = inputPrice.subtract(sdrOutput.getSdrPriceAmount());
                    log.info("ICL sdrDeltaPrice= " + deltaPrice);
                    BigDecimal durationBD = new BigDecimal(1);
                    durationBD = durationBD.multiply(deltaPrice);
                    log.info("ICL before divide durationBD= " + durationBD);
                    if (serviceCode.equals(SDRCode.SDRICLCZ1)) {
                        durationBD = durationBD.divide(priceICLCZ1, 2, RoundingMode.UP);
                        result.setServiceCode(SDRCode.SDRICLCZ1.name());
                    } else {
                        durationBD = durationBD.divide(priceICLCZ2, 2, RoundingMode.UP);
                        result.setServiceCode(SDRCode.SDRICLCZ2.name());
                    }
                    log.info("ICL durationBD= " + durationBD);
                    duration = durationBD.intValue();
                    log.info("ICL duration= " + duration);
                    result.setSdrDurationAmount(duration);
                    result.setEosNote("EOS termination");
                    result.setSdrEOS(true);
                    log.info("ICL EOS - true");
                }
                break;
            }
            case SDRRMGCZ1:
            case SDRRMGCZ2: {
                log.info("info - RMG");
                BigDecimal inputPrice = framesInput.getInputRmg();
                log.info("RMG pfAfPrice= " + inputPrice);
                BigDecimal thisSdrPrice = new BigDecimal(duration);
                if (serviceCode.equals(SDRCode.SDRRMGCZ1)) {
                    thisSdrPrice = thisSdrPrice.multiply(priceRMGCZ1);
                } else {
                    thisSdrPrice = thisSdrPrice.multiply(priceRMGCZ2);
                }
                log.info("RMG sdrPrice1= " + thisSdrPrice);
                BigDecimal outputPrice = sdrOutput.getSdrPriceAmount().add(thisSdrPrice);
                log.info("RMG sdrPrice= " + outputPrice);
                if (inputPrice.compareTo(outputPrice) < 0) {
                    BigDecimal deltaPrice = inputPrice.subtract(sdrOutput.getSdrPriceAmount());
                    log.info("RMG sdrDeltaPrice= " + deltaPrice);
                    BigDecimal durationBD = new BigDecimal(1);
                    durationBD = durationBD.multiply(deltaPrice);
                    log.info("RMG before divide durationBD= " + durationBD);
                    if (serviceCode.equals(SDRCode.SDRRMGCZ1)) {
                        durationBD = durationBD.divide(priceRMGCZ1, 2, RoundingMode.UP);
                        result.setServiceCode(SDRCode.SDRRMGCZ1.name());
                    } else {
                        durationBD = durationBD.divide(priceRMGCZ2, 2, RoundingMode.UP);
                        result.setServiceCode(SDRCode.SDRRMGCZ2.name());
                    }
                    log.info("RMG durationBD= " + durationBD);
                    duration = durationBD.intValue();
                    log.info("RMG duration= " + duration);
                    result.setSdrDurationAmount(duration);
                    result.setEosNote("EOS termination");
                    result.setSdrEOS(true);
                    log.info("RMG EOS - true");
                }
                break;
            }
        }
        log.info("result, duration = " + result.getSdrDurationAmount() + ", msgAmount = " + result.getSdrMsgAmount() + ", MBamount = " + result.getSdrMBamount() + ", price = " + result.getSdrPriceAmount() + ", note = " + result.getEosNote() + ", servoceCode = " + result.getServiceCode() + ", eos = " + result.isSdrEOS());
        return result;
    }

}


@Data
@NoArgsConstructor
@AllArgsConstructor
class FramesInputTotal {

    private int inputCls;
    private int inputSms;
    private BigDecimal inputInt;
    private BigDecimal inputAsm;
    private BigDecimal inputIcl;
    private BigDecimal inputRmg;

}


@Data
@NoArgsConstructor
@AllArgsConstructor
class SdrAmountCalc {

    private int sdrDurationAmount;
    private int sdrMsgAmount;
    private BigDecimal sdrMBamount;
    private BigDecimal sdrPriceAmount;
    private String eosNote;
    private String serviceCode;
    private boolean sdrEOS;
}