package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.AddonFrame;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.entity.ServiceDetailRecord;
import com.snezana.introtelecom.enums.AddonCode;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.exception.IllegalItemFieldException;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.AddonFrameRepo;
import com.snezana.introtelecom.repository.PackageFrameRepo;
import com.snezana.introtelecom.repository.ServiceDetailRecordRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FramesSDRValidationService {

    private final PackageFrameRepo packageFrameRepo;
    private final AddonFrameRepo addonFrameRepo;
    private final ServiceDetailRecordRepo serviceDetailRecordRepo;
    public static final String LOCAL_DATE_TIME_FORMAT = "'yyyy-MM-ddTHH:mm:ss.SSSZ'";

    public void controlTheLocalDateTimeInputIsValid(LocalDateTime localDateTime) {
        LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        if (localDateTime.equals(dateTime)) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.INVALID_DATE_TIME_FORMAT, String.format("Local DateTime format should be %s", LOCAL_DATE_TIME_FORMAT));
        }
    }

    public PackageFrame returnThePackageFrameIfExists (Long packfrId){
        return packageFrameRepo.findByPackfrIdOpt(packfrId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Package frame is not found."));
    }
    public PackageFrame returnTheMonthlyPackageFrameIfExists (String phoneNumber, LocalDateTime packfrStartDateTime, LocalDateTime packfrEndDateTime) {
        return packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(phoneNumber, packfrStartDateTime, packfrEndDateTime)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Monthly package frame is not found."));
    }

    public AddonFrame returnTheAddonFrameIfExists (Long addfrId){
        return addonFrameRepo.findByAddfrIdOpt(addfrId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Addon frame is not found."));
    }

    public ServiceDetailRecord returnTheServiceDetailRecordIfExists (Long sdrfrId){
        return serviceDetailRecordRepo.findBySdrIdOpt(sdrfrId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Service Detail Record is not found."));
    }

    public void controlTheStartTimeIsLessThanEndTime (LocalDateTime startDateTime, LocalDateTime endDateTime){
        if(startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime)){
            throw new IllegalItemFieldException(RestAPIErrorMessage.INVALID_STARTDATETIME_OR_ENDDATETIME, "Start DateTime must be before EndDateTime!");
        }
    }

    public void controlTheEndTimeIsLessThanEndOfTheMonth(LocalDateTime endDateTime){
        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime nextMonthDateTime = nowDateTime.plusMonths(1);
        LocalDateTime endOfTheMonth = LocalDateTime.of(nextMonthDateTime.getYear(), nextMonthDateTime.getMonth(), 1, 0, 0, 0, 0);
        if(endDateTime.isAfter(endOfTheMonth) || endDateTime.isEqual(endOfTheMonth)){
            throw new IllegalItemFieldException(RestAPIErrorMessage.INVALID_STARTDATETIME_OR_ENDDATETIME, "End DateTime must be before End of the month!");
        }
    }

    public void controlTheAddonFrameHasAlreadyGiven(String phoneNumber, String addonCode, LocalDateTime endDateTime){
        Optional<AddonFrame> addonFrameOptional = addonFrameRepo.findByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrEndDateTimeEquals(phoneNumber, addonCode, endDateTime);
        addonFrameOptional.ifPresent( addonFrame ->  {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "Addon frame with these parameters has already been given to this phone!");
        });
    }

    public void controlTheServiceDetailRecordAlreadyExists (String phoneNumber, String serviceCode, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Optional<ServiceDetailRecord> serviceDetailRecordOptional = serviceDetailRecordRepo.findServiceDetailRecordByPhone_PhoneNumberAndSdrStartDateTimeEqualsAndSdrEndDateTimeEqualsAndPhoneService_ServiceCode (phoneNumber, startDateTime, endDateTime, serviceCode);
        serviceDetailRecordOptional.ifPresent(serviceDetailRecord ->  {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "ServiceDetailRecord with these parameters already exists for this phone!");
        });
    }

    public void controlIsTheValidAddonFrameToThisPhone (String packageCodeStr, String addonCodeStr){
        PackagePlanType packagePlanType = PackagePlanType.findByKey(packageCodeStr);
        AddonCode addonCode = AddonCode.valueOf(addonCodeStr);
        if ((packagePlanType.equals(PackagePlanType.PRP01) && (addonCode.equals(AddonCode.ADDINT) || addonCode.equals(AddonCode.ADDASM) || addonCode.equals(AddonCode.ADDICL) || addonCode.equals(AddonCode.ADDRMG)))|| (packagePlanType.equals(PackagePlanType.PRP02) && (addonCode.equals(AddonCode.ADDASM) || addonCode.equals(AddonCode.ADDICL) || addonCode.equals(AddonCode.ADDRMG))) || (packagePlanType.equals(PackagePlanType.PST11) && addonCode.equals(AddonCode.ADDASM))){
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "The type of Addon Frame is not valid for this phone number!");
        }
    }

}
