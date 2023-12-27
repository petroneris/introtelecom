package com.snezana.introtelecom.controllers;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.repositories.UserRepo;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.security.JWTtokenGenerator;
import com.snezana.introtelecom.services.ClientService;
import com.snezana.introtelecom.services.CurrentInfoMonthlyBillFactsService;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import com.snezana.introtelecom.services.PackageAddonPhoneServService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
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
public class ClientController {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;
    private final PackageAddonPhoneServService packageAddonPhoneServService;

    @Operation(tags = "Client Controller", summary = "Get current info", description = "Get current info")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/getCurrentInfo" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<ClientCurrentInfo01ViewDTO>> getCurrentInfo(Authentication authentication){
        ClientCurrentInfo01ViewDTO clientCurrentInfo01ViewDTO = clientService.getCurrentInfo(authentication);
        return ResponseEntity.ok(RestAPIResponse.of(clientCurrentInfo01ViewDTO));
    }

    @Operation(tags = "Client Controller", summary = "Get MonthlyBillFacts by year and month", description = "Get MonthlyBillFacts by year and month")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/getMonthlyBillFactsByYearAndMonth" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<ClientMonthlyBillFactsPrpViewDTO>> getMonthlyBillFactsByYearMonth(Authentication authentication, @RequestParam int year, @RequestParam int month){
        ClientMonthlyBillFactsPrpViewDTO clientMonthlyBillFactsPrpViewDTO = clientService.getMonthlyBillFactsByYearAndMonth(authentication, year, month);
        return ResponseEntity.ok(RestAPIResponse.of(clientMonthlyBillFactsPrpViewDTO));
    }

    @Operation(tags = "Client Controller", summary = "Get MonthlyBillFact by StartDate, EndDate", description = "Get MonthlyBillFact by StartDate, EndDate")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/getMonthlyBillFactsFromStartDateToEndDate" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<? extends ClientMonthlyBillFactsPrpViewDTO>>> getMonthlyBillFactsStartDateToEndDate(Authentication authentication, @RequestParam int startYear, @RequestParam int startMonth, @RequestParam int endYear, @RequestParam int endMonth){
        List<? extends ClientMonthlyBillFactsPrpViewDTO> clientMonthlyBillFactsPrpViewDTOList = clientService.getMonthlyBillFactsFromStartDateToEndDate(authentication, startYear, startMonth, endYear, endMonth);
        return ResponseEntity.ok(RestAPIResponse.of( clientMonthlyBillFactsPrpViewDTOList));
    }

    @Operation(tags = "Client Controller", summary = "Get all packages")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/allPackagePlans_Info" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackagePlanDTO>>> getAllPackages(){
        List<PackagePlanDTO> packagePlanDTOList = clientService.getAllCustomersPackagePlans();
        return ResponseEntity.ok(RestAPIResponse.of(packagePlanDTOList));
    }

    @Operation(tags = "Client Controller", summary = "Get all AddOns")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/allAddOns_Info" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AddOnDTO>>> getAllAddOns(){
        List<AddOnDTO> addOnDTOList = packageAddonPhoneServService.getAllAddOns();
        return ResponseEntity.ok(RestAPIResponse.of(addOnDTOList));
    }

}
