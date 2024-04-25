package com.snezana.introtelecom.controller;

import com.snezana.introtelecom.dto.CurrentInfo1234ViewDTO;
import com.snezana.introtelecom.dto.MonthlyBillFactsViewDTO;
import com.snezana.introtelecom.service.CurrentInfoMonthlyBillFactsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CurrentInfoMonthlyBillFactsController.class)
@WithMockUser(username ="mika", roles="ADMIN")
class CurrentInfoMonthlyBillFactsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrentInfoMonthlyBillFactsService currentInfoMonthlyBillFactsService;


    @Test
    void testGetCurrentInfoByPhoneNumber() throws Exception {
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

        when(currentInfoMonthlyBillFactsService.getCurrentInfoByPhone(phoneNumber)).thenReturn(currentInfo1234ViewDTO);

        mockMvc.perform(get("/api/currentInfo/getCurrentInfoByPhoneNumber/{phoneNumber}", phoneNumber))
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
    void testGetMonthlyBillFactsById() throws Exception {
        Long monthlybillId = 1L;
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String month = "DECEMBER";
        int year = 2023;
        String monthlybillTotalprice = "1000.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);
        String monthlybillDateTimeStr = monthlybillDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        MonthlyBillFactsViewDTO monthlyBillFactsViewDTO = new MonthlyBillFactsViewDTO();
        monthlyBillFactsViewDTO.setMonthlybillId(monthlybillId);
        monthlyBillFactsViewDTO.setPhoneNumber(phoneNumber);
        monthlyBillFactsViewDTO.setPackageCode(packageCode);
        monthlyBillFactsViewDTO.setMonth(month);
        monthlyBillFactsViewDTO.setYear(year);
        monthlyBillFactsViewDTO.setMonthlybillTotalprice(monthlybillTotalprice);
        monthlyBillFactsViewDTO.setMonthlybillDateTime(monthlybillDateTime);

        when(currentInfoMonthlyBillFactsService.getMonthlyBillFactsById(monthlybillId)).thenReturn(monthlyBillFactsViewDTO);

        mockMvc.perform(get("/api/monthlyBillFacts/getMonthlyBillFactsById/{monthlybillId}", monthlybillId))
                .andExpect(jsonPath("$.data.monthlybillId").value(monthlybillId))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.packageCode").value(packageCode))
                .andExpect(jsonPath("$.data.month").value(month))
                .andExpect(jsonPath("$.data.year").value(year))
                .andExpect(jsonPath("$.data.monthlybillTotalprice").value(monthlybillTotalprice))
                .andExpect(jsonPath("$.data.monthlybillDateTime").value(monthlybillDateTimeStr))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetMonthlyBillFactsByPhoneAndYearMonth() throws Exception {
        String phoneNumber = "0769317426";
        String packageCode = "12";
        int month = 12;
        String monthStr = "DECEMBER";
        int year = 2023;
        String monthlybillTotalprice = "1000.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);
        String monthlybillDateTimeStr = monthlybillDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        MonthlyBillFactsViewDTO monthlyBillFactsViewDTO = new MonthlyBillFactsViewDTO();
        monthlyBillFactsViewDTO.setPhoneNumber(phoneNumber);
        monthlyBillFactsViewDTO.setPackageCode(packageCode);
        monthlyBillFactsViewDTO.setMonth(monthStr);
        monthlyBillFactsViewDTO.setYear(year);
        monthlyBillFactsViewDTO.setMonthlybillTotalprice(monthlybillTotalprice);
        monthlyBillFactsViewDTO.setMonthlybillDateTime(monthlybillDateTime);

        when(currentInfoMonthlyBillFactsService.getMonthlyBillFactsByPhoneAndYearAndMonth(phoneNumber, year, month)).thenReturn(monthlyBillFactsViewDTO);

        mockMvc.perform(get("/api/monthlyBillFacts/getMonthlyBillFactsByPhoneNumberYearAndMonth/{phoneNumber}", phoneNumber).param("year", String.valueOf(year)).param("month", String.valueOf(month)))
                .andExpect(jsonPath("$.data.phoneNumber").value(phoneNumber))
                .andExpect(jsonPath("$.data.packageCode").value(packageCode))
                .andExpect(jsonPath("$.data.month").value(monthStr))
                .andExpect(jsonPath("$.data.year").value(year))
                .andExpect(jsonPath("$.data.monthlybillTotalprice").value(monthlybillTotalprice))
                .andExpect(jsonPath("$.data.monthlybillDateTime").value(monthlybillDateTimeStr))
                .andExpect(jsonPath("$.responseDate").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testGetMonthlyBillFactsByPhoneFromStartDateToEndDate() throws Exception {
        String phoneNumber = "0769317426";
        String packageCode = "12";
        int month1 = 12;
        String month1Str = "DECEMBER";
        int year1 = 2023;
        String monthlybillTotalprice1 = "1000.00 cu";
        LocalDateTime monthlybillDateTime1 = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);
        MonthlyBillFactsViewDTO monthlyBillFactsViewDTO1 = new MonthlyBillFactsViewDTO();
        monthlyBillFactsViewDTO1.setPhoneNumber(phoneNumber);
        monthlyBillFactsViewDTO1.setPackageCode(packageCode);
        monthlyBillFactsViewDTO1.setMonth(month1Str);
        monthlyBillFactsViewDTO1.setYear(year1);
        monthlyBillFactsViewDTO1.setMonthlybillTotalprice(monthlybillTotalprice1);
        monthlyBillFactsViewDTO1.setMonthlybillDateTime(monthlybillDateTime1);
        int month2 = 1;
        String month2Str = "JANUARY";
        int year2 = 2024;
        String monthlybillTotalprice2 = "1000.00 cu";
        LocalDateTime monthlybillDateTime2 = LocalDateTime.of(2024, Month.FEBRUARY, 1, 0, 0, 0, 0);
        MonthlyBillFactsViewDTO monthlyBillFactsViewDTO2 = new MonthlyBillFactsViewDTO();
        monthlyBillFactsViewDTO2.setPhoneNumber(phoneNumber);
        monthlyBillFactsViewDTO2.setPackageCode(packageCode);
        monthlyBillFactsViewDTO2.setMonth(month2Str);
        monthlyBillFactsViewDTO2.setYear(year2);
        monthlyBillFactsViewDTO2.setMonthlybillTotalprice(monthlybillTotalprice2);
        monthlyBillFactsViewDTO2.setMonthlybillDateTime(monthlybillDateTime2);
        List<MonthlyBillFactsViewDTO> monthlyBillFactsViewDTOList = new ArrayList<>();
        monthlyBillFactsViewDTOList.add(monthlyBillFactsViewDTO1);
        monthlyBillFactsViewDTOList.add(monthlyBillFactsViewDTO2);

        when(currentInfoMonthlyBillFactsService.getMonthlyBillFactsByPhoneFromStartDateToEndDate(phoneNumber, year1, month1, year2, month2)).thenReturn(monthlyBillFactsViewDTOList);

        mockMvc.perform(get("/api/monthlyBillFacts/getMonthlyBillFactsByPhoneFromStartDateToEndDate/{phoneNumber}", phoneNumber).param("startYear", String.valueOf(year1)).param("startMonth", String.valueOf(month1)).param("endYear", String.valueOf(year2)).param("endMonth", String.valueOf(month2)))
                .andExpect(jsonPath("$.data.size()").value(monthlyBillFactsViewDTOList.size()))
                .andExpect(jsonPath("$.success").value(true));
    }
}