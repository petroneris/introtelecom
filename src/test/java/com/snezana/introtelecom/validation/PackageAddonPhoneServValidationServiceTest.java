package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.PhoneService;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.AddOnRepo;
import com.snezana.introtelecom.repository.PackagePlanRepo;
import com.snezana.introtelecom.repository.PhoneServiceRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PackageAddonPhoneServValidationServiceTest {

    @Mock
    private PackagePlanRepo packagePlanRepo;

    @Mock
    private AddOnRepo addOnRepo;

    @Mock
    private PhoneServiceRepo phoneServiceRepo;

    @InjectMocks
    private PackageAddonPhoneServValidationService packageAddonPhoneServValidationService;

    @Test
    void testControlThePackageCodeExists_exists() {
        String packageCode = "PRP01";
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(packageCode);
        when(packagePlanRepo.findByPackageCodeOpt(packageCode)).thenReturn(Optional.of(packagePlan));
        assertDoesNotThrow(() -> {
            packageAddonPhoneServValidationService.controlThePackageCodeExists(packageCode);
        });
    }

    @Test
    void testControlThePackageCodeExists_doesNotExist() {
        String packageCode = "UNKNW";
        when(packagePlanRepo.findByPackageCodeOpt(packageCode)).thenReturn(Optional.empty());
        String description = "Package code = " + packageCode + " is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            packageAddonPhoneServValidationService.controlThePackageCodeExists(packageCode);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnThePackagePlanIfPackageCodeExists_exists() {
        String packageCode = "PRP01";
        PackagePlan packagePlan = new PackagePlan();
        packagePlan.setPackageCode(packageCode);
        when(packagePlanRepo.findByPackageCodeOpt(packageCode)).thenReturn(Optional.of(packagePlan));
        PackagePlan newPackagePlan = packageAddonPhoneServValidationService.returnThePackagePlanIfPackageCodeExists(packageCode);
        assertThat(newPackagePlan.getPackageCode()).isEqualTo(packageCode);
    }

    @Test
    void testReturnThePackagePlanIfPackageCodeExists_doesNotExist() {
        String packageCode = "UNKNW";
        when(packagePlanRepo.findByPackageCodeOpt(packageCode)).thenReturn(Optional.empty());
        String description = "Package code = " + packageCode + " is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            packageAddonPhoneServValidationService.returnThePackagePlanIfPackageCodeExists(packageCode);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheAddOnCodeExists_exists() {
        String addonCode = "ADDCLS";
        AddOn addOn = new AddOn();
        addOn.setAddonCode(addonCode);
        when(addOnRepo.findByAddonCodeOpt(addonCode)).thenReturn(Optional.of(addOn));
        assertDoesNotThrow(() -> {
            packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonCode);
        });
    }

    @Test
    void testControlTheAddOnCodeExists_doesNotExist() {
        String addonCode = "UNKNW";
        when(addOnRepo.findByAddonCodeOpt(addonCode)).thenReturn(Optional.empty());
        String description = "Add-on code = " + addonCode + " is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonCode);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheAddOnIfAddonCodeExists_exists() {
        String addonCode = "ADDCLS";
        AddOn addOn = new AddOn();
        addOn.setAddonCode(addonCode);
        when(addOnRepo.findByAddonCodeOpt(addonCode)).thenReturn(Optional.of(addOn));
        AddOn newAddOn = packageAddonPhoneServValidationService.returnTheAddOnIfAddonCodeExists(addonCode);
        assertThat(newAddOn.getAddonCode()).isEqualTo(addonCode);
    }

    @Test
    void testReturnTheAddOnIfAddonCodeExists_doesNotExist() {
        String addonCode = "UNKNW";
        when(addOnRepo.findByAddonCodeOpt(addonCode)).thenReturn(Optional.empty());
        String description = "Add-on code = " + addonCode + " is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            packageAddonPhoneServValidationService.returnTheAddOnIfAddonCodeExists(addonCode);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlThePhoneServiceCodeExists_exists() {
        String serviceCode = "SDRCLS";
        PhoneService phoneService = new PhoneService();
        phoneService.setServiceCode(serviceCode);
        when(phoneServiceRepo.findByServiceCodeOpt(serviceCode)).thenReturn(Optional.of(phoneService));
        assertDoesNotThrow(() -> {
            packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceCode);
        });
    }

    @Test
    void testControlThePhoneServiceCodeExists_doesNotExist() {
        String serviceCode = "SDRCLS";
        when(phoneServiceRepo.findByServiceCodeOpt(serviceCode)).thenReturn(Optional.empty());
        String description = "Phone service code = " + serviceCode + " is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceCode);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnThePhoneServiceIfServiceCodeExists_exists() {
        String serviceCode = "SDRCLS";
        PhoneService phoneService = new PhoneService();
        phoneService.setServiceCode(serviceCode);
        when(phoneServiceRepo.findByServiceCodeOpt(serviceCode)).thenReturn(Optional.of(phoneService));
        PhoneService newPhoneService =  packageAddonPhoneServValidationService.returnThePhoneServiceIfServiceCodeExists(serviceCode);
        assertThat(newPhoneService.getServiceCode()).isEqualTo(serviceCode);
    }

    @Test
    void testReturnThePhoneServiceIfServiceCodeExists_doesNotExists() {
        String serviceCode = "SDRCLS";
        when(phoneServiceRepo.findByServiceCodeOpt(serviceCode)).thenReturn(Optional.empty());
        String description = "Phone service code = " + serviceCode + " is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            packageAddonPhoneServValidationService.returnThePhoneServiceIfServiceCodeExists(serviceCode);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }
}