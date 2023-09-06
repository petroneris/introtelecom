package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.AddOnRepo;
import com.snezana.introtelecom.repositories.PackagePlanRepo;
import com.snezana.introtelecom.repositories.PhoneServiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PackageAddonPhoneServValidationService {

    @Autowired
    PackagePlanRepo packagePlanRepo;

    @Autowired
    AddOnRepo addOnRepo;

    @Autowired
    PhoneServiceRepo phoneServiceRepo;

    public void controlThePackageCodeExists (String packageCode){
        packagePlanRepo.findByPackageCodeOpt(packageCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Package code = " + packageCode + " is not found."));
    }

    public void controlTheAddOnCodeExists (String addonCode){
        addOnRepo.findByAddonCodeOpt(addonCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "AddOn code = " + addonCode + " is not found."));
    }

    public void controlThePhoneServiceCodeExists (String serviceCode){
        phoneServiceRepo.findByServiceCodeOpt(serviceCode)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Phone service code = " + serviceCode + " is not found."));
    }

}
