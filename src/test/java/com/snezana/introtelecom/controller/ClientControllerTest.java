package com.snezana.introtelecom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snezana.introtelecom.dto.*;
import com.snezana.introtelecom.securitycontext.WithMockCustomUser;
import com.snezana.introtelecom.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ClientController.class)
@WithMockUser(username ="dana2", roles="CUSTOMER")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    @Test
    void testGetCurrentInfo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0747634418";
        String packageCode = "12";
        String currCls = "392 min left";
        String currSms = "398 msg left";
        String currInt = "8000.00 MB left";
        String currAsm = "3000.00 MB left";
        String currIcl = "200.00 cu left";
        String currRmg = "200.00 cu left";
        LocalDateTime currDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 22, 15, 10, 0, 0);
        String currDateTimeStr = currDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        CurrentInfo1234ViewDTO currentInfo1234ViewDTO = new CurrentInfo1234ViewDTO();
        currentInfo1234ViewDTO.setPhoneNumber(phoneNumber);
        currentInfo1234ViewDTO.setPackageCode(packageCode);
        currentInfo1234ViewDTO.setCurrCls(currCls);
        currentInfo1234ViewDTO.setCurrSms(currSms);
        currentInfo1234ViewDTO.setCurrInt(currInt);
        currentInfo1234ViewDTO.setCurrAsm(currAsm);
        currentInfo1234ViewDTO.setCurrIcl(currIcl);
        currentInfo1234ViewDTO.setCurrRmg(currRmg);
        currentInfo1234ViewDTO.setCurrDateTime(currDateTime);

        when(clientService.getCurrentInfo(authentication)).thenReturn(currentInfo1234ViewDTO);

        mockMvc.perform(get("/api/client/getCurrentInfo"))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.packageCode").value(packageCode))
                .andExpect(jsonPath("$.data.currCls").value(currCls))
                .andExpect(jsonPath("$.data.currSms").value(currSms))
                .andExpect(jsonPath("$.data.currInt").value(currInt))
                .andExpect(jsonPath("$.data.currAsm").value(currAsm))
                .andExpect(jsonPath("$.data.currIcl").value(currIcl))
                .andExpect(jsonPath("$.data.currRmg").value(currRmg))
                .andExpect(jsonPath("$.data.currDateTime").value(currDateTimeStr))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetMonthlyBillFactsByYearMonth_prepaid() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0769317426";
        String username = "dana2";
        String packageCode = "01";
        String packageCodeView = packageCode + "  -> THERE IS NO MONTHLY BILL FOR PREPAID PHONE PACKAGES.";
        String packageName = "prepaid01";
        int month = 12;
        int year = 2023;
        ClientMonthlyBillFactsPrpViewDTO clientMonthlyBillFactsPrpViewDTO = new ClientMonthlyBillFactsPrpViewDTO();
        clientMonthlyBillFactsPrpViewDTO.setPhoneNumber(phoneNumber);
        clientMonthlyBillFactsPrpViewDTO.setPackageCode(packageCodeView);
        clientMonthlyBillFactsPrpViewDTO.setUsername(username);
        clientMonthlyBillFactsPrpViewDTO.setPackageName(packageName);

        when(clientService.getMonthlyBillFactsByYearAndMonth(authentication, year, month)).thenReturn(clientMonthlyBillFactsPrpViewDTO);

        mockMvc.perform(get("/api/client/getMonthlyBillFactsByYearAndMonth").param("authentication", String.valueOf(authentication)).param("year", String.valueOf(year)).param("month", String.valueOf(month)))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.packageCode").value(packageCodeView))
                .andExpect(jsonPath("$.data.packageName").value(packageName))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetMonthlyBillFactsByYearMonth_postpaid() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0747634418";
        String username = "dana2";
        String packageCode = "12";
        String packageName = "postpaid12";
        int month = 12;
        String monthStr = "DECEMBER";
        int year = 2023;
        String monthlybillTotalprice = "1300.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);
        String monthlybillDateTimeStr = monthlybillDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ClientMonthlyBillFactsPstViewDTO clientMonthlyBillFactsPstViewDTO = new ClientMonthlyBillFactsPstViewDTO();
        clientMonthlyBillFactsPstViewDTO.setPhoneNumber(phoneNumber);
        clientMonthlyBillFactsPstViewDTO.setPackageCode(packageCode);
        clientMonthlyBillFactsPstViewDTO.setUsername(username);
        clientMonthlyBillFactsPstViewDTO.setPackageName(packageName);
        clientMonthlyBillFactsPstViewDTO.setMonth(monthStr);
        clientMonthlyBillFactsPstViewDTO.setYear(year);
        clientMonthlyBillFactsPstViewDTO.setMonthlybillTotalprice(monthlybillTotalprice);
        clientMonthlyBillFactsPstViewDTO.setMonthlybillDateTime(monthlybillDateTime);

        when(clientService.getMonthlyBillFactsByYearAndMonth(authentication, year, month)).thenReturn(clientMonthlyBillFactsPstViewDTO);

        mockMvc.perform(get("/api/client/getMonthlyBillFactsByYearAndMonth").param("authentication", String.valueOf(authentication)).param("year", String.valueOf(year)).param("month", String.valueOf(month)))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.username").value(username))
                .andExpect(jsonPath("$.data.packageCode").value(packageCode))
                .andExpect(jsonPath("$.data.packageName").value(packageName))
                .andExpect(jsonPath("$.data.month").value(monthStr))
                .andExpect(jsonPath("$.data.year").value(year))
                .andExpect(jsonPath("$.data.monthlybillTotalprice").value(monthlybillTotalprice))
                .andExpect(jsonPath("$.data.monthlybillDateTime").value(monthlybillDateTimeStr))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetMonthlyBillFactsStartDateToEndDate_prepaid() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0769317426";
        String username = "dana2";
        String packageCode = "01";
        String packageCodeView = packageCode + "  -> THERE IS NO MONTHLY BILL FOR PREPAID PHONE PACKAGES.";
        String packageName = "prepaid01";
        int month1 = 12;
        int year1 = 2023;
        int month2 = 1;
        int year2 = 2024;
        ClientMonthlyBillFactsPrpViewDTO clientMonthlyBillFactsPrpViewDTO1 = new ClientMonthlyBillFactsPrpViewDTO();
        clientMonthlyBillFactsPrpViewDTO1.setPhoneNumber(phoneNumber);
        clientMonthlyBillFactsPrpViewDTO1.setPackageCode(packageCodeView);
        clientMonthlyBillFactsPrpViewDTO1.setUsername(username);
        clientMonthlyBillFactsPrpViewDTO1.setPackageName(packageName);
        List<ClientMonthlyBillFactsPrpViewDTO> clientMonthlyBillFactsPrpViewDTOList = new ArrayList<>();
        clientMonthlyBillFactsPrpViewDTOList.add(clientMonthlyBillFactsPrpViewDTO1);

        doReturn(clientMonthlyBillFactsPrpViewDTOList).when(clientService).getMonthlyBillFactsFromStartDateToEndDate(authentication, year1, month1, year2, month2);

        mockMvc.perform(get("/api/client/getMonthlyBillFactsFromStartDateToEndDate").param("authentication", String.valueOf(authentication)).param("startYear", String.valueOf(year1)).param("startMonth", String.valueOf(month1)).param("endYear", String.valueOf(year2)).param("endMonth", String.valueOf(month2)))
                .andExpect(jsonPath("$.data[0].phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data[0].username").value(username))
                .andExpect(jsonPath("$.data[0].packageCode").value(packageCodeView))
                .andExpect(jsonPath("$.data[0].packageName").value(packageName))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetMonthlyBillFactsStartDateToEndDate_postpaid() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = "0747634418";
        String username = "dana2";
        String packageCode = "12";
        String packageName = "postpaid12";
        int month1 = 12;
        String month1Str = "DECEMBER";
        int year1 = 2023;
        String monthlybillTotalprice1 = "1300.00 cu";
        LocalDateTime monthlybillDateTime1 = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);
        ClientMonthlyBillFactsPstViewDTO clientMonthlyBillFactsPstViewDTO1 = new ClientMonthlyBillFactsPstViewDTO();
        clientMonthlyBillFactsPstViewDTO1.setPhoneNumber(phoneNumber);
        clientMonthlyBillFactsPstViewDTO1.setPackageCode(packageCode);
        clientMonthlyBillFactsPstViewDTO1.setUsername(username);
        clientMonthlyBillFactsPstViewDTO1.setPackageName(packageName);
        clientMonthlyBillFactsPstViewDTO1.setMonth(month1Str);
        clientMonthlyBillFactsPstViewDTO1.setYear(year1);
        clientMonthlyBillFactsPstViewDTO1.setMonthlybillTotalprice(monthlybillTotalprice1);
        clientMonthlyBillFactsPstViewDTO1.setMonthlybillDateTime(monthlybillDateTime1);
        int month2 = 1;
        String month2Str = "JANUARY";
        int year2 = 2024;
        String monthlybillTotalprice2 = "1000.00 cu";
        LocalDateTime monthlybillDateTime2 = LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0, 0);
        ClientMonthlyBillFactsPstViewDTO clientMonthlyBillFactsPstViewDTO2 = new ClientMonthlyBillFactsPstViewDTO();
        clientMonthlyBillFactsPstViewDTO2.setPhoneNumber(phoneNumber);
        clientMonthlyBillFactsPstViewDTO2.setPackageCode(packageCode);
        clientMonthlyBillFactsPstViewDTO2.setUsername(username);
        clientMonthlyBillFactsPstViewDTO2.setPackageName(packageName);
        clientMonthlyBillFactsPstViewDTO2.setMonth(month2Str);
        clientMonthlyBillFactsPstViewDTO2.setYear(year2);
        clientMonthlyBillFactsPstViewDTO2.setMonthlybillTotalprice(monthlybillTotalprice2);
        clientMonthlyBillFactsPstViewDTO2.setMonthlybillDateTime(monthlybillDateTime2);
        List<ClientMonthlyBillFactsPstViewDTO> clientMonthlyBillFactsPstViewDTOList = new ArrayList<>();
        clientMonthlyBillFactsPstViewDTOList.add(clientMonthlyBillFactsPstViewDTO1);
        clientMonthlyBillFactsPstViewDTOList.add(clientMonthlyBillFactsPstViewDTO2);

        doReturn(clientMonthlyBillFactsPstViewDTOList).when(clientService).getMonthlyBillFactsFromStartDateToEndDate(authentication, year1, month1, year2, month2);

        mockMvc.perform(get("/api/client/getMonthlyBillFactsFromStartDateToEndDate").param("authentication", String.valueOf(authentication)).param("startYear", String.valueOf(year1)).param("startMonth", String.valueOf(month1)).param("endYear", String.valueOf(year2)).param("endMonth", String.valueOf(month2)))
                .andExpect(jsonPath("$.data.size()").value(clientMonthlyBillFactsPstViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testChangePassword() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String oldPassword = "newWorld";
        String newPassword = "freshNewWorld";
        String checkNewPassword = "freshNewWorld";
        ClientChangePasswordDTO clientChangePasswordDTO = new ClientChangePasswordDTO();
        clientChangePasswordDTO.setOldPassword(oldPassword);
        clientChangePasswordDTO.setNewPassword(newPassword);
        clientChangePasswordDTO.setCheckNewPassword(checkNewPassword);
        String message = "The password is changed.";

        doNothing().when(clientService).changePassword(authentication, clientChangePasswordDTO);

        mockMvc.perform(patch("/api/client/changePassword")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientChangePasswordDTO)))
                .andExpect(jsonPath("$.data.message").value(message))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllPackages() throws Exception {
        PackagePlanDTO packagePlanDTO1 = new PackagePlanDTO();
        packagePlanDTO1.setPackageCode("01");
        packagePlanDTO1.setPackageName("prepaid1");
        packagePlanDTO1.setPackageDescription("description01");
        packagePlanDTO1.setPackagePrice("200.00");
        PackagePlanDTO packagePlanDTO2 = new PackagePlanDTO();
        packagePlanDTO2.setPackageCode("02");
        packagePlanDTO2.setPackageName("prepaid2");
        packagePlanDTO2.setPackageDescription("description02");
        packagePlanDTO2.setPackagePrice("300.00");
        List<PackagePlanDTO> packagePlanDTOList = new ArrayList<>();
        packagePlanDTOList.add(packagePlanDTO1);
        packagePlanDTOList.add(packagePlanDTO2);

        when(clientService.getAllCustomersPackagePlans()).thenReturn(packagePlanDTOList);

        mockMvc.perform(get("/api/client/allPackagePlans_Info"))
                .andExpect(jsonPath("$.data.size()").value(packagePlanDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetAllAddOns() throws Exception {
        String addonCode1 = "ADDCLS";
        String addonDescription1 = "description01";
        String addonPrice1 = "200.00";
        AddOnDTO addOnDTO1 = new AddOnDTO();
        addOnDTO1.setAddonCode(addonCode1);
        addOnDTO1.setAddonDescription(addonDescription1);
        addOnDTO1.setAddonPrice(addonPrice1);
        String addonCode2 = "ADDSMS";
        String addonDescription2 = "description02";
        String addonPrice2 = "300.00";
        AddOnDTO addOnDTO2 = new AddOnDTO();
        addOnDTO2.setAddonCode(addonCode2);
        addOnDTO2.setAddonDescription(addonDescription2);
        addOnDTO2.setAddonPrice(addonPrice2);
        List<AddOnDTO> addOnDTOList = new ArrayList<>();
        addOnDTOList.add(addOnDTO1);
        addOnDTOList.add(addOnDTO2);

        when(clientService.getAllAddOnDetails()).thenReturn(addOnDTOList);

        mockMvc.perform(get("/api/client/allAddOns_Info"))
                .andExpect(jsonPath("$.data.size()").value(addOnDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }
}