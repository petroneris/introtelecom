package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.response.RestMessage;
import com.snezana.introtelecom.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;

    @Operation(tags = "Client Controller", description = "Get current info")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/getCurrentInfo" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<CurrentInfo01ViewDTO>> getCurrentInfo(Authentication authentication){
        CurrentInfo01ViewDTO clientCurrentInfo01ViewDTO = clientService.getCurrentInfo(authentication);
        return ResponseEntity.ok(RestAPIResponse.of(clientCurrentInfo01ViewDTO));
    }

    @Operation(tags = "Client Controller", description = "Get monthly bill by year and month")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/getMonthlyBillFactsByYearAndMonth" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<ClientMonthlyBillFactsPrpViewDTO>> getMonthlyBillFactsByYearMonth(Authentication authentication, @RequestParam int year, @RequestParam int month){
        ClientMonthlyBillFactsPrpViewDTO clientMonthlyBillFactsPrpViewDTO = clientService.getMonthlyBillFactsByYearAndMonth(authentication, year, month);
        return ResponseEntity.ok(RestAPIResponse.of(clientMonthlyBillFactsPrpViewDTO));
    }

    @Operation(tags = "Client Controller", description = "Get monthly bill by start year and month, end year and month")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/getMonthlyBillFactsFromStartDateToEndDate" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<? extends ClientMonthlyBillFactsPrpViewDTO>>> getMonthlyBillFactsStartDateToEndDate(Authentication authentication, @RequestParam int startYear, @RequestParam int startMonth, @RequestParam int endYear, @RequestParam int endMonth){
        List<? extends ClientMonthlyBillFactsPrpViewDTO> clientMonthlyBillFactsPrpViewDTOList = clientService.getMonthlyBillFactsFromStartDateToEndDate(authentication, startYear, startMonth, endYear, endMonth);
        return ResponseEntity.ok(RestAPIResponse.of( clientMonthlyBillFactsPrpViewDTOList));
    }

    @Operation(tags = "Client Controller", description = "Change a password")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping(value = "/client/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<Map<String, String>>> changePassword(Authentication authentication, @RequestBody @Valid ClientChangePasswordDTO clientChangePasswordDto){
        clientService.changePassword(authentication, clientChangePasswordDto);
        String message = "The password is changed.";
        return ResponseEntity.ok(RestAPIResponse.of(RestMessage.view(message)));
    }

    @Operation(tags = "Client Controller", description = "Get all packages")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/allPackagePlans_Info" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<PackagePlanDTO>>> getAllPackages(){
        List<PackagePlanDTO> packagePlanDTOList = clientService.getAllCustomersPackagePlans();
        return ResponseEntity.ok(RestAPIResponse.of(packagePlanDTOList));
    }

    @Operation(tags = "Client Controller", description = "Get all add-ons")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "client/allAddOns_Info" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<AddOnDTO>>> getAllAddOns(){
        List<AddOnDTO> addOnDTOList = clientService.getAllAddOnDetails();
        return ResponseEntity.ok(RestAPIResponse.of(addOnDTOList));
    }

}
