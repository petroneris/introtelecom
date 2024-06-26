package com.snezana.introtelecom.validation;

import com.snezana.introtelecom.entity.MonthlyBillFacts;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.exception.IllegalItemFieldException;
import com.snezana.introtelecom.exception.ItemNotFoundException;
import com.snezana.introtelecom.exception.RestAPIErrorMessage;
import com.snezana.introtelecom.repository.MonthlyBillFactsRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonthlyBillFactsValidationServiceTest {

    @Mock
    private MonthlyBillFactsRepo monthlyBillFactsRepo;

    @InjectMocks
    private MonthlyBillFactsValidationService monthlyBillFactsValidationService;

    @Test
    void testReturnTheMonthlyBillFactsByIdIfExists_exists() {
        Long id = 3L;
        MonthlyBillFacts monthlyBillFacts = new MonthlyBillFacts();
        monthlyBillFacts.setMonthlybillId(id);
        when(monthlyBillFactsRepo.findByMonthlybillIdOpt(id)).thenReturn(Optional.of(monthlyBillFacts));
        MonthlyBillFacts newMonthlyBillFacts = monthlyBillFactsValidationService.returnTheMonthlyBillFactsByIdIfExists(id);
        assertThat(newMonthlyBillFacts.getMonthlybillId()).isEqualTo(id);
    }

    @Test
    void testReturnTheMonthlyBillFactsByIdIfExists_doesNotExist() {
        Long id = 3L;
        when(monthlyBillFactsRepo.findByMonthlybillIdOpt(id)).thenReturn(Optional.empty());
        String description = "Monthly bill with this id is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            monthlyBillFactsValidationService.returnTheMonthlyBillFactsByIdIfExists(id);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testReturnTheMonthlyBillFactsByYearMonthIfExists_exists() {
        String phoneNumber = "0732283498";
        LocalDate yearMonth = LocalDate.of(2020, Month.JANUARY, 1);
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneNumber);
        MonthlyBillFacts monthlyBillFacts = new MonthlyBillFacts();
        monthlyBillFacts.setPhone(phone);
        monthlyBillFacts.setYearMonth(yearMonth);
        when(monthlyBillFactsRepo.findByPhoneNumberAndMonthAndYearOpt(phoneNumber, yearMonth)).thenReturn(Optional.of(monthlyBillFacts));
        MonthlyBillFacts newMonthlyBillFacts = monthlyBillFactsValidationService.returnTheMonthlyBillFactsByYearMonthIfExists(phoneNumber, yearMonth);
        assertThat(newMonthlyBillFacts.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(newMonthlyBillFacts.getYearMonth()).isEqualTo(yearMonth);
    }

    @Test
    void testReturnTheMonthlyBillFactsByYearMonthIfExists_doesNotExist() {
        String phoneNumber = "0732283498";
        LocalDate yearMonth = LocalDate.of(2020, Month.JANUARY, 1);
        when(monthlyBillFactsRepo.findByPhoneNumberAndMonthAndYearOpt(phoneNumber, yearMonth)).thenReturn(Optional.empty());
        String description = "Monthly bill with this year and month is not found.";
        ItemNotFoundException exception = assertThrows(ItemNotFoundException.class, () -> {
            monthlyBillFactsValidationService.returnTheMonthlyBillFactsByYearMonthIfExists(phoneNumber, yearMonth);
        });
        assertEquals(RestAPIErrorMessage.ITEM_NOT_FOUND, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testCheckYearAndMonth_wrongYear() {
        int year = 2020;
        int month = 11;
        String description = "Year must be between 2021 and " + LocalDate.now().getYear();
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            monthlyBillFactsValidationService.checkYearAndMonth (year, month);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testCheckYearAndMonth_wrongMonth() {
        int year = 2023;
        int month = 13;
        String description = "Month must be between 1 and 12";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            monthlyBillFactsValidationService.checkYearAndMonth (year, month);
        });
        assertEquals(RestAPIErrorMessage.WRONG_ITEM, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testCheckYearAndMonth_okYearMonth() {
        int year = 2023;
        int month = 10;
        assertDoesNotThrow(() -> {
            monthlyBillFactsValidationService.checkYearAndMonth (year, month);
        });
    }

    @Test
    void testControlTheStartDateIsLessThanEndDate_itIs() {
        LocalDate startDate = LocalDate.of(2023, Month.JANUARY, 20);
        LocalDate endDate = LocalDate.of(2023, Month.FEBRUARY, 20);
        assertDoesNotThrow(() -> {
            monthlyBillFactsValidationService.controlTheStartDateIsLessThanEndDate (startDate, endDate);
        });
    }

    @Test
    void testControlTheStartDateIsLessThanEndDate_itIsNot() {
        LocalDate startDate = LocalDate.of(2023, Month.FEBRUARY, 20);
        LocalDate endDate = LocalDate.of(2023, Month.JANUARY, 20);
        String description = "StartDate must be before EndDate!";
        IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
            monthlyBillFactsValidationService.controlTheStartDateIsLessThanEndDate (startDate, endDate);
        });
        assertEquals(RestAPIErrorMessage.INVALID_STARTDATE_OR_ENDDATE, exception.getErrorMessage());
        assertEquals(description, exception.getDescription());
    }

    @Test
    void testControlTheTimeForScheduling_timeIsValid_after12oclockOnDayOfMonth1() {
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2023, Month.MARCH, 22, 15, 10, 0, 0);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class))).thenReturn(fixedLocalDateTime);
            assertDoesNotThrow(() -> {
                monthlyBillFactsValidationService.controlTheTimeForScheduling();
            });
        }
    }

    @Test
    void testControlTheTimeForScheduling_timeIsNotValid_before12oclockOnDayOfMonth1() {
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2023, Month.MARCH, 1, 9, 10, 0, 0);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class))).thenReturn(fixedLocalDateTime);
            String description = "Data are not available until 12 o'clock today!";
            IllegalItemFieldException exception = assertThrows(IllegalItemFieldException.class, () -> {
                monthlyBillFactsValidationService.controlTheTimeForScheduling();
            });
            assertEquals(RestAPIErrorMessage.INVALID_DATETIME_INTERVAL, exception.getErrorMessage());
            assertEquals(description, exception.getDescription());
        }
    }

}