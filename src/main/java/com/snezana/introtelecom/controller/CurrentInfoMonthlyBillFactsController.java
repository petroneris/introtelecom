package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.CurrentInfo01ViewDTO;
import com.snezana.introtelecom.dto.MonthlyBillFactsViewDTO;
import com.snezana.introtelecom.response.RestAPIResponse;
import com.snezana.introtelecom.service.CurrentInfoMonthlyBillFactsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@RequiredArgsConstructor
public class CurrentInfoMonthlyBillFactsController {

//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CurrentInfoMonthlyBillFactsController.class);

    private final CurrentInfoMonthlyBillFactsService currentInfoMonthlyBillFactsService;

    @Operation(tags = "CurrentInfoMonthlyBillFacts Controller", description = "Get current info by phone number")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "currentInfo/getCurrentInfoByPhoneNumber/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<CurrentInfo01ViewDTO>> getCurrentInfoByPhoneNumber(@PathVariable String phoneNumber){
        CurrentInfo01ViewDTO currentInfo01ViewDTO = currentInfoMonthlyBillFactsService.getCurrentInfoByPhone(phoneNumber);
        return ResponseEntity.ok(RestAPIResponse.of(currentInfo01ViewDTO));
    }

    @Operation(tags = "CurrentInfoMonthlyBillFacts Controller", description = "Get monthly bill by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "monthlyBillFacts/getMonthlyBillFactsById/{monthlybillId}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<MonthlyBillFactsViewDTO>> getMonthlyBillFactsById(@PathVariable Long monthlybillId){
        MonthlyBillFactsViewDTO monthlyBillFactsViewDTO = currentInfoMonthlyBillFactsService.getMonthlyBillFactsById(monthlybillId);
        return ResponseEntity.ok(RestAPIResponse.of( monthlyBillFactsViewDTO));
    }

    @Operation(tags = "CurrentInfoMonthlyBillFacts Controller", description = "Get monthly bills by phone number, year and month")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "monthlyBillFacts/getMonthlyBillFactsByPhoneNumberYearAndMonth/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<MonthlyBillFactsViewDTO>> getMonthlyBillFactsByPhoneAndYearMonth(@PathVariable String phoneNumber, @RequestParam int year, @RequestParam int month){
        MonthlyBillFactsViewDTO monthlyBillFactsViewDTO = currentInfoMonthlyBillFactsService.getMonthlyBillFactsByPhoneAndYearAndMonth(phoneNumber, year, month);
        return ResponseEntity.ok(RestAPIResponse.of(monthlyBillFactsViewDTO));
    }

    @Operation(tags = "CurrentInfoMonthlyBillFacts Controller", description = "Get monthly bills by phone number, start year and month, end year and month")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping(value = "monthlyBillFacts/getMonthlyBillFactsByPhoneFromStartDateToEndDate/{phoneNumber}" , produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestAPIResponse<List<MonthlyBillFactsViewDTO>>> getMonthlyBillFactsByPhoneFromStartDateToEndDate(@PathVariable String phoneNumber, @RequestParam int startYear, @RequestParam int startMonth, @RequestParam int endYear, @RequestParam int endMonth){
        List<MonthlyBillFactsViewDTO> monthlyBillFactsViewDTOList = currentInfoMonthlyBillFactsService.getMonthlyBillFactsByPhoneFromStartDateToEndDate(phoneNumber, startYear, startMonth, endYear, endMonth);
        return ResponseEntity.ok(RestAPIResponse.of(monthlyBillFactsViewDTOList));
    }

}
