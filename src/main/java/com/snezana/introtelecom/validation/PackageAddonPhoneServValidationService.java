package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.PhoneService;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.AddOnRepo;
import com.snezana.introtelecom.repository.PackagePlanRepo;
import com.snezana.introtelecom.repository.PhoneServiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageAddonPhoneServValidationService {

    private final PackagePlanRepo packagePlanRepo;
    private final AddOnRepo addOnRepo;
    private final PhoneServiceRepo phoneServiceRepo;

    public void controlThePackageCodeExists (String packageCode){
        packagePlanRepo.findByPackageCodeOpt(packageCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Package code = " + packageCode + " is not found."));
    }

    public PackagePlan returnThePackagePlanIfPackageCodeExists (String packageCode){
        return packagePlanRepo.findByPackageCodeOpt(packageCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Package code = " + packageCode + " is not found."));
    }

    public void controlTheAddOnCodeExists (String addonCode){
        addOnRepo.findByAddonCodeOpt(addonCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Add-on code = " + addonCode + " is not found."));
    }

    public AddOn returnTheAddOnIfAddonCodeExists (String addonCode){
        return addOnRepo.findByAddonCodeOpt(addonCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Add-on code = " + addonCode + " is not found."));
    }

    public void controlThePhoneServiceCodeExists (String serviceCode){
        phoneServiceRepo.findByServiceCodeOpt(serviceCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Phone service code = " + serviceCode + " is not found."));
    }

    public PhoneService returnThePhoneServiceIfServiceCodeExists (String serviceCode){
        return phoneServiceRepo.findByServiceCodeOpt(serviceCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Phone service code = " + serviceCode + " is not found."));
    }

}
