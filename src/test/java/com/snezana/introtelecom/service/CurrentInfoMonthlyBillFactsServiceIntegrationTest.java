package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.CurrentInfo1234ViewDTO;
import com.snezana.introtelecom.dto.MonthlyBillFactsViewDTO;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CurrentInfoMonthlyBillFactsServiceIntegrationTest {

    @Autowired
    private CurrentInfoMonthlyBillFactsService  currentInfoMonthlyBillFactsService;

    // current time is mocked to fixedLocalDateTime
    @Test
    void testGetCurrentInfoByPhone() {
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String currCls = "392 min left";
        String currSms = "398 msg left";
        String currInt = "8000.00 MB left";
        String currAsm = "3000.00 MB left";
        String currIcl = "200.00 cu left";
        String currRmg = "200.00 cu left";
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2023, Month.FEBRUARY, 22, 15, 10, 0, 0);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class))).thenReturn(fixedLocalDateTime);
            CurrentInfo1234ViewDTO currentInfo1234ViewDTO = (CurrentInfo1234ViewDTO) currentInfoMonthlyBillFactsService.getCurrentInfoByPhone(phoneNumber);
            assertThat(currentInfo1234ViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
            assertThat(currentInfo1234ViewDTO.getPackageCode()).isEqualTo(packageCode);
            assertThat(currentInfo1234ViewDTO.getCurrCls()).isEqualTo(currCls);
            assertThat(currentInfo1234ViewDTO.getCurrSms()).isEqualTo(currSms);
            assertThat(currentInfo1234ViewDTO.getCurrInt()).isEqualTo(currInt);
            assertThat(currentInfo1234ViewDTO.getCurrAsm()).isEqualTo(currAsm);
            assertThat(currentInfo1234ViewDTO.getCurrIcl()).isEqualTo(currIcl);
            assertThat(currentInfo1234ViewDTO.getCurrRmg()).isEqualTo(currRmg);
            assertThat(currentInfo1234ViewDTO.getCurrDateTime()).isEqualTo(fixedLocalDateTime);
        }
    }

    @Test
    void testGetMonthlyBillFactsById(){
        Long id = 1L;
        String phoneNumber = "0769317426";
        String packageCode = "12";
        String month = "DECEMBER";
        int year = 2023;
        String monthlybillTotalprice = "1000.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);
        MonthlyBillFactsViewDTO monthlyBillFactsViewDTO = currentInfoMonthlyBillFactsService.getMonthlyBillFactsById(id);
        assertThat(monthlyBillFactsViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(monthlyBillFactsViewDTO.getPackageCode()).isEqualTo(packageCode);
        assertThat(monthlyBillFactsViewDTO.getMonth()).isEqualTo(month);
        assertThat(monthlyBillFactsViewDTO.getYear()).isEqualTo(year);
        assertThat(monthlyBillFactsViewDTO.getMonthlybillTotalprice()).isEqualTo(monthlybillTotalprice);
        assertThat(monthlyBillFactsViewDTO.getMonthlybillDateTime()).isEqualTo(monthlybillDateTime);
    }

    @Test
    void testGetMonthlyBillFactsByPhoneAndYearAndMonth(){
        String phoneNumber = "0769317426";
        String packageCode = "12";
        int month = 12;
        int year = 2023;
        String monthStr = "DECEMBER";
        String monthlybillTotalprice = "1000.00 cu";
        LocalDateTime monthlybillDateTime = LocalDateTime.of(2024, Month.JANUARY, 1, 0, 0, 0, 0);
        MonthlyBillFactsViewDTO monthlyBillFactsViewDTO = currentInfoMonthlyBillFactsService.getMonthlyBillFactsByPhoneAndYearAndMonth(phoneNumber, year, month);
        assertThat(monthlyBillFactsViewDTO.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(monthlyBillFactsViewDTO.getPackageCode()).isEqualTo(packageCode);
        assertThat(monthlyBillFactsViewDTO.getMonth()).isEqualTo(monthStr);
        assertThat(monthlyBillFactsViewDTO.getYear()).isEqualTo(year);
        assertThat(monthlyBillFactsViewDTO.getMonthlybillTotalprice()).isEqualTo(monthlybillTotalprice);
        assertThat(monthlyBillFactsViewDTO.getMonthlybillDateTime()).isEqualTo(monthlybillDateTime);
    }

    @Test
    void testGetMonthlyBillFactsByPhoneFromStartDateToEndDate(){
        int size = 3;
        String phoneNumber = "0769317426";
        int startMonth = 12;
        int startYear = 2023;
        int endMonth = 2;
        int endYear = 2024;
        List<MonthlyBillFactsViewDTO> monthlyBillFactsViewDTOList = currentInfoMonthlyBillFactsService.getMonthlyBillFactsByPhoneFromStartDateToEndDate(phoneNumber, startYear, startMonth, endYear, endMonth);
        assertThat(monthlyBillFactsViewDTOList).isNotEmpty();
        assertThat(monthlyBillFactsViewDTOList.size()).isEqualTo(size);
    }

}
