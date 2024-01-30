package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.AddOnDTO;
import com.snezana.introtelecom.dto.PackagePlanDTO;
import com.snezana.introtelecom.dto.PhoneServiceDTO;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.response.RestMessage;
import com.snezana.introtelecom.service.PackageAddonPhoneServService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
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
@RequiredArgsConstructor
public class PackagesAddOnsPhoneServicesController {

//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(PackagesAddOnsPhoneServicesController.class);
    private final PackageAddonPhoneServService packageAddonPhoneServService;

    /**
     * some of the methods for Package plans, Add-ons and (Phone) Services
     */

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Get package by package code")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "package/getPackagePlanByPackageCode/{packageCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<PackagePlanDTO>> getPackageByPackageCode(@PathVariable String packageCode){
        PackagePlanDTO packagePlanDTO = packageAddonPhoneServService.getPackagePlanByPackageCode(packageCode);
        return ResponseEntity.ok(RestAPIResponse.of(packagePlanDTO));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Get all packages")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "package/getAllPackagePlans" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackagePlanDTO>>> getAllPackages(){
        List<PackagePlanDTO> packagePlanDTOList = packageAddonPhoneServService.getAllPackagePlans();
        return ResponseEntity.ok(RestAPIResponse.of(packagePlanDTOList));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Change package price")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "package/changePackagePrice/{packageCode}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changePackagePrice(@PathVariable String packageCode, @RequestParam BigDecimal packagePrice ){
        packageAddonPhoneServService.changePackagePrice(packageCode, packagePrice);
        String message = "The package price of package code= " +packageCode + " is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Get Add-on by addon code")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addon/getAddOnByAddOnCode/{addonCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<AddOnDTO>> getAddOnByAddOnCode(@PathVariable String addonCode){
        AddOnDTO addOnDTO = packageAddonPhoneServService.getAddOnByAddOnCode(addonCode);
        return ResponseEntity.ok(RestAPIResponse.of(addOnDTO));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Get all Add-ons")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "addon/getAllAddOns" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AddOnDTO>>> getAllAddOns(){
        List<AddOnDTO> addOnDTOList = packageAddonPhoneServService.getAllAddOns();
        return ResponseEntity.ok(RestAPIResponse.of(addOnDTOList));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Change addon price")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "addon/changeAddOnPrice/{addonCode}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changeAddOnPrice(@PathVariable String addonCode, @RequestParam BigDecimal addonPrice ){
        packageAddonPhoneServService.changeAddOnPrice(addonCode, addonPrice);
        String message = "The addon price of addon code= " +addonCode + " is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Get phone service by service code")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "service/getPhoneServiceByServiceCode/{serviceCode}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<PhoneServiceDTO>> getPhoneServiceByServiceCode(@PathVariable String serviceCode){
        PhoneServiceDTO phoneServiceDTO = packageAddonPhoneServService.getPhoneServiceByServiceCode(serviceCode);
        return ResponseEntity.ok(RestAPIResponse.of(phoneServiceDTO));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Get all (Phone) Services")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "service/getAllPhoneServices" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PhoneServiceDTO>>> getAllPhoneServices(){
        List<PhoneServiceDTO> phoneServiceDTOList = packageAddonPhoneServService.getAllPhoneServices();
        return ResponseEntity.ok(RestAPIResponse.of(phoneServiceDTOList));
    }

    @Operation(tags = "PackagesAddOnsPhoneServices Controller", description = "Change service price")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "service/changeServicePrice/{serviceCode}")
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changeServicePrice(@PathVariable String serviceCode, @RequestParam BigDecimal servicePrice ){
        packageAddonPhoneServService.changePhoneServicePrice(serviceCode, servicePrice);
        String message = "The service price of service code= " +serviceCode + " is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

}
