package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.*;
import com.snezana.introtelecom.exception.IllegalItemFieldException;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.AddonFrameRepo;
import com.snezana.introtelecom.repository.PackageFrameRepo;
import com.snezana.introtelecom.repository.ServiceDetailRecordRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FramesSDRValidationServiceTest {

    @Mock
    private PackageFrameRepo packageFrameRepo;

    @Mock
    private AddonFrameRepo addonFrameRepo;

    @Mock
    private ServiceDetailRecordRepo serviceDetailRecordRepo;

    @InjectMocks
    private FramesSDRValidationService framesSDRValidationService;

    public static final String LOCAL_DATE_TIME_FORMAT = "'yyyy-MM-ddTHH:mm:ss.SSSZ'";

    @Test
    void testControlTheLocalDateTimeInputIsValid_isValid() {
        LocalDateTime dateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        assertDoesNotThrow(() -> {
            framesSDRValidationService.controlTheLocalDateTimeInputIsValid(dateTime);
        });
    }

    @Test
    void testControlTheLocalDateTimeInputIsValid_isNotValid() {
        LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        String description = String.format("Local DateTime format should be %s", LOCAL_DATE_TIME_FORMAT);
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            framesSDRValidationService.controlTheLocalDateTimeInputIsValid(dateTime);
        });
        assertEquals(RestAPIErrorMessage.INVALID_DATE_TIME_FORMAT, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnThePackageFrameIfExists_exists() {
        Long id = 3L;
        PackageFrame packageFrame = new PackageFrame();
        packageFrame.setPackfrId(id);
        when(packageFrameRepo.findByPackfrIdOpt(id)).thenReturn(Optional.of(packageFrame));
        PackageFrame newPackageFrame = framesSDRValidationService.returnThePackageFrameIfExists(id);
        assertThat(newPackageFrame.getPackfrId()).isEqualTo(id);
    }

    @Test
    void testReturnThePackageFrameIfExists_doesNotExist() {
        Long id = 3L;
        when(packageFrameRepo.findByPackfrIdOpt(id)).thenReturn(Optional.empty());
        String description = "Package frame is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            framesSDRValidationService.returnThePackageFrameIfExists(id);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheMonthlyPackageFrameIfExists_exists() {
        String phoneNumber = "0732283498";
        LocalDateTime startDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0, 0);
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        PackageFrame packageFrame = new PackageFrame();
        packageFrame.setPhone(phone);
        packageFrame.setPackfrStartDateTime(startDateTime);
        packageFrame.setPackfrEndDateTime(endDateTime);
        when(packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(phoneNumber, startDateTime, endDateTime)).thenReturn(Optional.of(packageFrame));
        PackageFrame newPackageFrame = framesSDRValidationService.returnTheMonthlyPackageFrameIfExists(phoneNumber, startDateTime, endDateTime);
        assertThat(newPackageFrame.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(newPackageFrame.getPackfrStartDateTime()).isEqualTo(startDateTime);
        assertThat(newPackageFrame.getPackfrEndDateTime()).isEqualTo(endDateTime);
    }

    @Test
    void testReturnTheMonthlyPackageFrameIfExists_doesNotExists() {
        String phoneNumber = "0732283498";
        LocalDateTime startDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0, 0);
        when(packageFrameRepo.findPackageFrameByPhone_PhoneNumberAndPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(phoneNumber, startDateTime, endDateTime)).thenReturn(Optional.empty());
        String description = "Monthly package frame is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            framesSDRValidationService.returnTheMonthlyPackageFrameIfExists(phoneNumber, startDateTime, endDateTime);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheAddonFrameIfExists_exists() {
        Long id = 3L;
        AddonFrame addonFrame = new AddonFrame();
        addonFrame.setAddfrId(id);
        when(addonFrameRepo.findByAddfrIdOpt(id)).thenReturn(Optional.of(addonFrame));
        AddonFrame newAddonFrame = framesSDRValidationService.returnTheAddonFrameIfExists(id);
        assertThat(newAddonFrame.getAddfrId()).isEqualTo(id);
    }

    @Test
    void testReturnTheAddonFrameIfExists_doesNotExist() {
        Long id = 3L;
        when(addonFrameRepo.findByAddfrIdOpt(id)).thenReturn(Optional.empty());
        String description = "Addon frame is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            framesSDRValidationService.returnTheAddonFrameIfExists(id);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheServiceDetailRecordIfExists_exists() {
        Long id = 3L;
        ServiceDetailRecord serviceDetailRecord = new ServiceDetailRecord();
        serviceDetailRecord.setSdrId(id);
        when(serviceDetailRecordRepo.findBySdrIdOpt(id)).thenReturn(Optional.of(serviceDetailRecord));
        ServiceDetailRecord newServiceDetailRecord = framesSDRValidationService.returnTheServiceDetailRecordIfExists(id);
        assertThat(newServiceDetailRecord.getSdrId()).isEqualTo(id);
    }

    @Test
    void testReturnTheServiceDetailRecordIfExists_doesNotExist() {
        Long id = 3L;
        when(serviceDetailRecordRepo.findBySdrIdOpt(id)).thenReturn(Optional.empty());
        String description = "Service Detail Record is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            framesSDRValidationService.returnTheServiceDetailRecordIfExists(id);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheStartTimeIsLessThanEndTime_itIs() {
        LocalDateTime startDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0, 0);
        assertDoesNotThrow(() -> {
            framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(startDateTime, endDateTime);
        });
    }

    @Test
    void testControlTheStartTimeIsLessThanEndTime_itIsNot() {
        LocalDateTime startDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        String description = "Start DateTime must be before EndDateTime!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            framesSDRValidationService.controlTheStartTimeIsLessThanEndTime(startDateTime, endDateTime);
        });
        assertEquals(RestAPIErrorMessage.INVALID_STARTDATETIME_OR_ENDDATETIME, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheEndTimeIsLessThanEndOfTheMonth_itIs() {
        LocalDateTime endDateTime = LocalDateTime.now();
        assertDoesNotThrow(() -> {
            framesSDRValidationService.controlTheEndTimeIsLessThanEndOfTheMonth(endDateTime);
        });
    }

    @Test
    void testControlTheEndTimeIsLessThanEndOfTheMonth_itIsNot() {
        LocalDateTime endDateTime = LocalDateTime.now().plusMonths(1);
        String description = "End DateTime must be before End of the month!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            framesSDRValidationService.controlTheEndTimeIsLessThanEndOfTheMonth(endDateTime);
        });
        assertEquals(RestAPIErrorMessage.INVALID_STARTDATETIME_OR_ENDDATETIME, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheAddonFrameHasAlreadyGiven_itHasNot() {
        String phoneNumber = "0732283498";
        String addonCode = "ADDCLS";
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        when(addonFrameRepo.findByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrEndDateTimeEquals(phoneNumber, addonCode, endDateTime)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            framesSDRValidationService.controlTheAddonFrameHasAlreadyGiven(phoneNumber, addonCode, endDateTime);
        });
    }

    @Test
    void testControlTheAddonFrameHasAlreadyGiven_itHas() {
        String phoneNumber = "0732283498";
        String addonCode = "ADDCLS";
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        AddonFrame addonFrame = new AddonFrame();
        addonFrame.setPhone(phone);
        addonFrame.setAddfrEndDateTime(endDateTime);
        when(addonFrameRepo.findByPhone_PhoneNumberAndAddOn_AddonCodeAndAddfrEndDateTimeEquals(phoneNumber, addonCode, endDateTime)).thenReturn(Optional.of(addonFrame));
        String description = "Addon frame with these parameters has already been given to this phone!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            framesSDRValidationService.controlTheAddonFrameHasAlreadyGiven(phoneNumber, addonCode, endDateTime);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheServiceDetailRecordAlreadyExists_exists() {
        String phoneNumber = "0732283498";
        String serviceCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0, 0);
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        PhoneService phoneService = new PhoneService();
        phoneService.setServiceCode(serviceCode);
        ServiceDetailRecord serviceDetailRecord = new ServiceDetailRecord();
        serviceDetailRecord.setPhone(phone);
        serviceDetailRecord.setPhoneService(phoneService);
        serviceDetailRecord.setSdrStartDateTime(startDateTime);
        serviceDetailRecord.setSdrEndDateTime(endDateTime);
        when(serviceDetailRecordRepo.findServiceDetailRecordByPhone_PhoneNumberAndSdrStartDateTimeEqualsAndSdrEndDateTimeEqualsAndPhoneService_ServiceCode (phoneNumber, startDateTime, endDateTime, serviceCode)).thenReturn(Optional.of(serviceDetailRecord));
        String description = "ServiceDetailRecord with these parameters already exists for this phone!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            framesSDRValidationService.controlTheServiceDetailRecordAlreadyExists(phoneNumber, serviceCode, startDateTime, endDateTime);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheServiceDetailRecordAlreadyExists_doesNotExist() {
        String phoneNumber = "0732283498";
        String serviceCode = "ADDCLS";
        LocalDateTime startDateTime = LocalDateTime.of(2023, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 1, 0, 0, 0);
        when(serviceDetailRecordRepo.findServiceDetailRecordByPhone_PhoneNumberAndSdrStartDateTimeEqualsAndSdrEndDateTimeEqualsAndPhoneService_ServiceCode (phoneNumber, startDateTime, endDateTime, serviceCode)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            framesSDRValidationService.controlTheServiceDetailRecordAlreadyExists(phoneNumber, serviceCode, startDateTime, endDateTime);
        });
    }

    @Test
    void testControlIsTheValidAddonFrameToThisPhone_isValid() {
        String packageCode = "12";
        String addonCode = "ADDASM";
        assertDoesNotThrow(() -> {
            framesSDRValidationService.controlIsTheValidAddonFrameToThisPhone(packageCode, addonCode);
        });
    }

    @Test
    void testControlIsTheValidAddonFrameToThisPhone_isNotValid() {
        String packageCode = "11";
        String addonCode = "ADDASM";
        String description = "The type of Addon Frame is not valid for this phone number!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            framesSDRValidationService.controlIsTheValidAddonFrameToThisPhone(packageCode, addonCode);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

}