package com.snezana.introtelecom.controllers;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.AdminViewDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;
import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.response.RestMessage;
import com.snezana.introtelecom.services.PackageAddonPhoneServService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class PackagesAddOnsPhoneServicesController {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PackagesAddOnsPhoneServicesController.class);

    @Autowired
    private PackageAddonPhoneServService packageAddonPhoneServService;

    /*
    some of the main methods for Package Plans, Add-Ons and (Phone) Services
    there are plenty methods that they can apply to
     */

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Get package by packageCode")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "package/getPackagePlanByPackageCode/{packageCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<PackagePlanDTO>> getPackageByPackageCode(@PathVariable String packageCode){
        PackagePlanDTO packagePlanDTO = packageAddonPhoneServService.getPackagePlanByPackageCode(packageCode);
        return ResponseEntity.ok(RestAPIResponse.of(packagePlanDTO));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Get all packages")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "package/getAllPackagePlans" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackagePlanDTO>>> getAllPackages(){
        List<PackagePlanDTO> packagePlanDTOList = packageAddonPhoneServService.getAllPackagePlans();
        return ResponseEntity.ok(RestAPIResponse.of(packagePlanDTOList));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Change package price")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "package/changePackagePrice/{packageCode}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changePackagePrice(@PathVariable String packageCode, @RequestParam BigDecimal packagePrice ){
        packageAddonPhoneServService.changePackagePrice(packageCode, packagePrice);
        String message = "The packagePrice of packageCode= " +packageCode + " is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Get AddOn by addonCode")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addon/getAddOnByAddOnCode/{addonCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<AddOnDTO>> getAddOnByAddOnCode(@PathVariable String addonCode){
        AddOnDTO addOnDTO = packageAddonPhoneServService.getAddOnByAddOnCode(addonCode);
        return ResponseEntity.ok(RestAPIResponse.of(addOnDTO));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Get all AddOns")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addon/getAllAddOns" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AddOnDTO>>> getAllAddOns(){
        List<AddOnDTO> addOnDTOList = packageAddonPhoneServService.getAllAddOns();
        return ResponseEntity.ok(RestAPIResponse.of(addOnDTOList));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Change addon price")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "addon/changeAddOnPrice/{addonCode}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changeAddOnPrice(@PathVariable String addonCode, @RequestParam BigDecimal addonPrice ){
        packageAddonPhoneServService.changeAddOnPrice(addonCode, addonPrice);
        String message = "The addonPrice of addonCode= " +addonCode + " is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Get phone service by serviceCode")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "service/getPhoneServiceByServiceCode/{serviceCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<PhoneServiceDTO>> getPhoneServiceByServiceCode(@PathVariable String serviceCode){
        PhoneServiceDTO phoneServiceDTO = packageAddonPhoneServService.getPhoneServiceByServiceCode(serviceCode);
        return ResponseEntity.ok(RestAPIResponse.of(phoneServiceDTO));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Get all PhoneServices")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "service/getAllPhoneServices" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PhoneServiceDTO>>> getAllPhoneServices(){
        List<PhoneServiceDTO> phoneServiceDTOList = packageAddonPhoneServService.getAllPhoneServices();
        return ResponseEntity.ok(RestAPIResponse.of(phoneServiceDTOList));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", summary = "Change service price")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "service/changeServicePrice/{serviceCode}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changeServicePrice(@PathVariable String serviceCode, @RequestParam BigDecimal servicePrice ){
        packageAddonPhoneServService.changePhoneServicePrice(serviceCode, servicePrice);
        String message = "The servicePrice of serviceCode= " +serviceCode + " is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }


}
