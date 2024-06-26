package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.enums.PackagePlanType;
import com.snezana.introtelecom.exception.IllegalItemFieldException;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.PhoneRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PhoneValidationServiceTest {

    @Mock
    private PhoneRepo phoneRepo;

    @InjectMocks
    private PhoneValidationService phoneValidationService;

    @Test
    void testControlThePhoneNumberIsUnique_notUnique() {
        Phone phone = new Phone();
        String phoneNumber = "0732283498";
        phone.setPhoneNumber(phoneNumber);
        String description = "The phone number = " + phoneNumber + " already exists in database!";
        when(phoneRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(phone));
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            phoneValidationService.controlThePhoneNumberIsUnique(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_IS_NOT_UNIQUE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlThePhoneNumberIsUnique_isUnique() {
        String phoneNumber = "0732283498";
        when(phoneRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> {
            phoneValidationService.controlThePhoneNumberIsUnique(phoneNumber);
        });
    }

    @Test
    void testControlThePhoneExists_exists() {
        Phone phone = new Phone();
        String phoneNumber = "0732283498";
        phone.setPhoneNumber(phoneNumber);
        when(phoneRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(phone));
        assertDoesNotThrow(() -> {
            phoneValidationService.controlThePhoneExists(phoneNumber);
        });
    }

    @Test
    void testControlThePhoneExists_doesNotExists() {
        String phoneNumber = "0732283498";
        String description = "The phone number = " +phoneNumber + " doesn't exist in database!";
        when(phoneRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            phoneValidationService.controlThePhoneExists(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnThePhoneIfExists_exists() {
        Phone phone = new Phone();
        String phoneNumber = "0732283498";
        phone.setPhoneNumber(phoneNumber);
        when(phoneRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.of(phone));
        Phone newPhone = phoneValidationService.returnThePhoneIfExists(phoneNumber);
        assertThat(newPhone.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testReturnThePhoneIfExists_doesNotExists() {
        String phoneNumber = "0732283498";
        String description = "The phone number = " +phoneNumber + " doesn't exist in database!";
        when(phoneRepo.findByPhoneNumberOpt(phoneNumber)).thenReturn(Optional.empty());
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            phoneValidationService.returnThePhoneIfExists(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlThisPhoneHasTheAdminPackageCode_itHas() {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.ADM.getPackageCode());
        Phone phone = new Phone();
        phone.setPackagePlan(packagePlan);
        assertDoesNotThrow(() -> {
            phoneValidationService.controlThisPhoneHasTheAdminPackageCode(phone);
        });
    }

    @Test
    void testControlThisPhoneHasTheAdminPackageCode_itHasNot() {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.PST13.getPackageCode());
        Phone phone = new Phone();
        phone.setPackagePlan(packagePlan);
        String description = "This phone number has no admin package code!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            phoneValidationService.controlThisPhoneHasTheAdminPackageCode(phone);
        });
        assertEquals(RestAPIErrorMessage.ITEMS_NOT_MATCH, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlThisPhoneHasCustomersPackageCode_itHas() {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.PST13.getPackageCode());
        Phone phone = new Phone();
        phone.setPackagePlan(packagePlan);
        assertDoesNotThrow(() -> {
            phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
        });
    }

    @Test
    void testControlThisPhoneHasCustomersPackageCode_itHasNot() {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.ADM.getPackageCode());
        Phone phone = new Phone();
        phone.setPackagePlan(packagePlan);
        String description = "This phone number has no customer's package code!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            phoneValidationService.controlThisPhoneHasCustomersPackageCode(phone);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlThisPhoneHasPostpaidPackageCode_hasPostpaid() {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.PST13.getPackageCode());
        Phone phone = new Phone();
        String phoneNumber = "0732283498";
        phone.setPhoneNumber(phoneNumber);
        phone.setPackagePlan(packagePlan);
        when(phoneRepo.findByPhoneNumber(phoneNumber)).thenReturn(phone);
        assertDoesNotThrow(() -> {
            phoneValidationService.controlThisPhoneHasPostpaidPackageCode(phoneNumber);
        });
    }

    @Test
    void testControlThisPhoneHasPostpaidPackageCode_hasNotPostpaid() {
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(PackagePlanType.PRP01.getPackageCode());
        Phone phone = new Phone();
        String phoneNumber = "0732283498";
        phone.setPhoneNumber(phoneNumber);
        phone.setPackagePlan(packagePlan);
        String description = "The monthly bill is not provided for the package plan related to this phone number!";
        when(phoneRepo.findByPhoneNumber(phoneNumber)).thenReturn(phone);
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            phoneValidationService.controlThisPhoneHasPostpaidPackageCode(phoneNumber);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

}