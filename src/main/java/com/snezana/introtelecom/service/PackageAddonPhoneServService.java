package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;

import java.math.BigDecimal;
import java.util.List;

public interface PackageAddonPhoneServService {

    PackagePlanDTO getPackagePlanByPackageCode (String packageCode);

    List<PackagePlanDTO> getAllPackagePlans();

    void changePackagePrice (String packageCode, BigDecimal packagePrice);

    AddOnDTO getAddOnByAddOnCode (String addonCode);

    List<AddOnDTO> getAllAddOns();

    void changeAddOnPrice (String addonCode, BigDecimal addonPrice);

    PhoneServiceDTO getPhoneServiceByServiceCode (String serviceCode);

    List<PhoneServiceDTO> getAllPhoneServices();

    void changePhoneServicePrice (String serviceCode, BigDecimal servicePrice);

}
