package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.entity.AddonFrame;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.entity.ServiceDetailRecord;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.AddonFrameRepo;
import com.snezana.introtelecom.repositories.PackageFrameRepo;
import com.snezana.introtelecom.repositories.ServiceDetailRecordRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FramesSDRValidationService {

    @Autowired
    PackageFrameRepo packageFrameRepo;

    @Autowired
    AddonFrameRepo addonFrameRepo;

    @Autowired
    ServiceDetailRecordRepo serviceDetailRecordRepo;

    public static final String LOCAL_DATE_TIME_FORMAT = "'yyyy-MM-ddTHH:mm:ss.SSSZ'";

    public void controlTheLocalDateTimeInputIsValid(LocalDateTime localDateTime) {
        LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        if (localDateTime.equals(dateTime)) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.INVALID_DATE_TIME_FORMAT, String.format("Local DateTime format should be %s", LOCAL_DATE_TIME_FORMAT));
        }
    }

    public void controlThePackageFrameExists (Long packfrId){
     packageFrameRepo.findByPackfrIdOpt(packfrId)
             .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Package frame is not found."));
    }

    public void controlTheAddonFrameExists (Long addfrId){
        addonFrameRepo.findByAddfrIdOpt(addfrId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Addon frame is not found."));
    }

    public void controlTheServiceDetailRecordExists (Long sdrfrId){
        serviceDetailRecordRepo.findBySdrIdOpt(sdrfrId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Service Detail Record is not found."));
    }

    public void controlTheStartTimeIsLessThanEndTime (LocalDateTime startDateTime, LocalDateTime endDateTime){
        if(startDateTime.isAfter(endDateTime) || startDateTime.isEqual(endDateTime)){
            throw new IllegalItemFieldException(RestAPIErrorMessage.INVALID_STARTDATETIME_OR_ENDDATETIME, "StartDateTime must be less than EndDateTime!");
        }
    }

    public void controlTheMonthlyPackageFrameExistsAlready (String phoneNumber, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Optional<PackageFrame> packageFrameOptional = packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeGreaterThanEqualAndPackfrEndDateTimeLessThanEqual(phoneNumber, startDateTime, endDateTime);
        if (packageFrameOptional.isPresent()) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "The monthly PackageFrame already exists!");
        }
    }

    public void controlTheAddonFrameHasAlreadyGiven(String phoneNumber, String addonCode, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Optional<AddonFrame> addonFrameOptional = addonFrameRepo.findByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrStartDateTimeGreaterThanEqualAndAddfrEndDateTimeLessThanEqual(phoneNumber, addonCode, startDateTime, endDateTime);
        if (addonFrameOptional.isPresent()) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "AddonFrame with these parameters already exists for that phone!");
        }
    }

    public void controlTheServiceDetailRecordAlreadyExists (String phoneNumber, String serviceCode, LocalDateTime startDateTime, LocalDateTime endDateTime){
        Optional<ServiceDetailRecord> serviceDetailRecordOptional = serviceDetailRecordRepo.findServiceDetailRecordByPhone_PhoneNumberAndSdrStartDateTimeEqualsAndSdrEndDateTimeEqualsAndPhoneService_ServiceCode (phoneNumber, startDateTime, endDateTime, serviceCode);
        if (serviceDetailRecordOptional.isPresent()) {
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "ServiceDetailRecord with these parameters already exists for that phone!");
        }
    }

}
