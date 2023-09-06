package com.snezana.introtelecom.services;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;
import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.PhoneService;
import com.snezana.introtelecom.mapper.AddOnMapper;
import com.snezana.introtelecom.mapper.PackagePlanMapper;
import com.snezana.introtelecom.mapper.PhoneServiceMapper;
import com.snezana.introtelecom.repositories.AddOnRepo;
import com.snezana.introtelecom.repositories.PackagePlanRepo;
import com.snezana.introtelecom.repositories.PhoneServiceRepo;
import com.snezana.introtelecom.validations.PackageAddonPhoneServValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PackageAddonPhoneServServiceImpl implements  PackageAddonPhoneServService{

    @Autowired
    PackageAddonPhoneServValidationService packageAddonPhoneServValidationService;

    @Autowired
    PackagePlanRepo packagePlanRepo;

    @Autowired
    AddOnRepo addOnRepo;

    @Autowired
    PhoneServiceRepo phoneServiceRepo;

    @Autowired
    PackagePlanMapper packagePlanMapper;

    @Autowired
    AddOnMapper addOnMapper;

    @Autowired
    PhoneServiceMapper phoneServiceMapper;

    @Override
    public PackagePlanDTO getPackagePlanByPackageCode(String packageCode) {
       packageAddonPhoneServValidationService.controlThePackageCodeExists(packageCode);
       PackagePlan packagePlan = packagePlanRepo.findByPackageCode(packageCode);
       PackagePlanDTO packagePlanDTO = packagePlanMapper.packagePlanToPackagePlanDTO(packagePlan);
       return packagePlanDTO;
    }

    @Override
    public List<PackagePlanDTO> getAllPackagePlans() {
        List<PackagePlan> packagePlanList = packagePlanRepo.findAll();
        List<PackagePlanDTO> packagePlanDTOList = packagePlanMapper.packagePlanToPackagePlanDTO(packagePlanList);
        return packagePlanDTOList;
    }

    @Override
    public void changePackagePrice(String packageCode, BigDecimal packagePrice) {
        packageAddonPhoneServValidationService.controlThePackageCodeExists(packageCode);
        PackagePlan packagePlan = packagePlanRepo.findByPackageCode(packageCode);
        packagePlan.setPackagePrice(packagePrice);
        packagePlanRepo.save(packagePlan);
    }

    @Override
    public AddOnDTO getAddOnByAddOnCode(String addonCode) {
        packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonCode);
        AddOn addOn = addOnRepo.findByAddonCode(addonCode);
        AddOnDTO addOnDTO = addOnMapper.addOnToAddOnDTO(addOn);
        return addOnDTO;
    }

    @Override
    public List<AddOnDTO> getAllAddOns() {
        List<AddOn> addOnList = addOnRepo.findAll();
        List<AddOnDTO> addOnDTOList = addOnMapper.addOnToAddOnDTO(addOnList);
        return addOnDTOList;
    }

    @Override
    public void changeAddOnPrice(String addonCode, BigDecimal addonPrice) {
        packageAddonPhoneServValidationService.controlTheAddOnCodeExists(addonCode);
        AddOn addOn = addOnRepo.findByAddonCode(addonCode);
        addOn.setAddonPrice(addonPrice);
        addOnRepo.save(addOn);
    }

    @Override
    public PhoneServiceDTO getPhoneServiceByServiceCode(String serviceCode) {
        packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceCode);
        PhoneService phoneService = phoneServiceRepo.findByServiceCode(serviceCode);
        PhoneServiceDTO phoneServiceDTO = phoneServiceMapper.phoneServiceToPhoneServiceDTO(phoneService);
        return phoneServiceDTO;
    }

    @Override
    public List<PhoneServiceDTO> getAllPhoneServices() {
        List<PhoneService> phoneServiceList = phoneServiceRepo.findAll();
        List<PhoneServiceDTO> phoneServiceDTOList = phoneServiceMapper.phoneServiceToPhoneServiceDTO(phoneServiceList);
        return phoneServiceDTOList;
    }

    @Override
    public void changePhoneServicePrice(String serviceCode, BigDecimal servicePrice) {
        packageAddonPhoneServValidationService.controlThePhoneServiceCodeExists(serviceCode);
        PhoneService phoneService = phoneServiceRepo.findByServiceCode(serviceCode);
        phoneService.setServicePrice(servicePrice);
        phoneServiceRepo.save(phoneService);
    }
}
