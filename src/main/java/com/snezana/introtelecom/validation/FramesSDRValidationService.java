package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.AddonFrame;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.entity.ServiceDetailRecord;
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
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "The monthly package frame is not found."));
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

    public void controlTheAddonFrameHasAlreadyGiven(String phoneNumber, String addonCode, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Optional<AddonFrame> addonFrameOptional = addonFrameRepo.findByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(phoneNumber, addonCode, startDateTime, endDateTime);
        addonFrameOptional.ifPresent( addonFrame ->  {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "Addon frame with these parameters has already given for this phone!");
        });
    }

    public void controlTheServiceDetailRecordAlreadyExists (String phoneNumber, String serviceCode, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Optional<ServiceDetailRecord> serviceDetailRecordOptional = serviceDetailRecordRepo.findServiceDetailRecordByPhone_PhoneNumberAndSdrStartDateTimeEqualsAndSdrEndDateTimeEqualsAndPhoneService_ServiceCode (phoneNumber, startDateTime, endDateTime, serviceCode);
        serviceDetailRecordOptional.ifPresent(serviceDetailRecord ->  {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "ServiceDetailRecord with these parameters already exists for this phone!");
        });
    }

}
