package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;
import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.PhoneService;
import com.snezana.introtelecom.mapper.AddOnMapper;
import com.snezana.introtelecom.mapper.PackagePlanMapper;
import com.snezana.introtelecom.mapper.PhoneServiceMapper;
import com.snezana.introtelecom.repository.AddOnRepo;
import com.snezana.introtelecom.repository.PackagePlanRepo;
import com.snezana.introtelecom.repository.PhoneServiceRepo;
import com.snezana.introtelecom.validation.PackageAddonPhoneServValidationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PackageAddonPhoneServService{
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PackageAddonPhoneServService.class);

    private final PackageAddonPhoneServValidationService packageAddonPhoneServValidationService;
    private final PackagePlanRepo packagePlanRepo;
    private final AddOnRepo addOnRepo;
    private final PhoneServiceRepo phoneServiceRepo;
    private final PackagePlanMapper packagePlanMapper;
    private final AddOnMapper addOnMapper;
    private final PhoneServiceMapper phoneServiceMapper;

    public PackagePlanDTO getPackagePlanByPackageCode(String packageCode) {
        PackagePlan packagePlan = packageAddonPhoneServValidationService.returnThePackagePlanIfPackageCodeExists(packageCode);
        log.info(packagePlan.getPackageCode());
        log.info(packagePlan.getPackageName());
        log.info(packagePlan.getPackagePrice().toString());
        return packagePlanMapper.packagePlanToPackagePlanDTO(packagePlan);
    }

    public List<PackagePlanDTO> getAllPackagePlans() {
        List<PackagePlan> packagePlanList = packagePlanRepo.findAll();
        return packagePlanMapper.packagePlanToPackagePlanDTO(packagePlanList);
    }

    public void changePackagePrice(String packageCode, BigDecimal packagePrice) {
        PackagePlan packagePlan = packageAddonPhoneServValidationService.returnThePackagePlanIfPackageCodeExists(packageCode);
        packagePlan.setPackagePrice(packagePrice);
        packagePlanRepo.save(packagePlan);
    }

    public AddOnDTO getAddOnByAddOnCode(String addonCode) {
        AddOn addOn = packageAddonPhoneServValidationService.returnTheAddOnIfAddonCodeExists(addonCode);
        return addOnMapper.addOnToAddOnDTO(addOn);
    }

    public List<AddOnDTO> getAllAddOns() {
        List<AddOn> addOnList = addOnRepo.findAll();
        return addOnMapper.addOnToAddOnDTO(addOnList);
    }

    public void changeAddOnPrice(String addonCode, BigDecimal addonPrice) {
        AddOn addOn = packageAddonPhoneServValidationService.returnTheAddOnIfAddonCodeExists(addonCode);
        addOn.setAddonPrice(addonPrice);
        addOnRepo.save(addOn);
    }

    public PhoneServiceDTO getPhoneServiceByServiceCode(String serviceCode) {
        PhoneService phoneService = packageAddonPhoneServValidationService.returnThePhoneServiceIfServiceCodeExists(serviceCode);
        return phoneServiceMapper.phoneServiceToPhoneServiceDTO(phoneService);
    }

    public List<PhoneServiceDTO> getAllPhoneServices() {
        List<PhoneService> phoneServiceList = phoneServiceRepo.findAll();
        return phoneServiceMapper.phoneServiceToPhoneServiceDTO(phoneServiceList);
    }

    public void changePhoneServicePrice(String serviceCode, BigDecimal servicePrice) {
        PhoneService phoneService = packageAddonPhoneServValidationService.returnThePhoneServiceIfServiceCodeExists(serviceCode);
        phoneService.setServicePrice(servicePrice);
        phoneServiceRepo.save(phoneService);
    }
}
