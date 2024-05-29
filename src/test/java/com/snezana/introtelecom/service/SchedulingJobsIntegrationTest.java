package com.snezana.introtelecom.service;

import com.snezana.introtelecom.entity.MonthlyBillFacts;
import com.snezana.introtelecom.entity.PackageFrame;
import com.snezana.introtelecom.repository.MonthlyBillFactsRepo;
import com.snezana.introtelecom.repository.PackageFrameRepo;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SchedulingJobsIntegrationTest {
    // current time is mocked to fixedLocalDateTime

    @Autowired
    private SchedulingJobs schedulingJobs;

    @Autowired
    private PackageFrameRepo packageFrameRepo;

    @Autowired
    private MonthlyBillFactsRepo monthlyBillFactsRepo;

    @Test
    @Order(1)
    void testCreateMonthlyPackageFrame(){
        int size = 24;
        LocalDateTime startDateTime = LocalDateTime.of(2024, Month.MARCH, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, Month.APRIL, 1, 0, 0, 0, 0);
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2024, Month.MARCH, 1, 0, 1, 0, 0);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class))).thenReturn(fixedLocalDateTime);
            schedulingJobs.createMonthlyPackageFrame();
            List<PackageFrame> packageFrameList = packageFrameRepo.findByPackfrStartDateTimeEqualsAndPackfrEndDateTimeEquals(startDateTime, endDateTime);
            assertThat(packageFrameList).hasSize(size);
        }
    }

    @Test
    @Order(2)
    @Sql(scripts = {"/delete_monthlybill_facts.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(scripts = {"/delete_package_frame.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCreateMonthlyBillFacts(){
        int size = 16;
        LocalDateTime fixedLocalDateTime = LocalDateTime.of(2024, Month.APRIL, 1, 10, 1, 0, 0);
        LocalDate yearMonth = LocalDate.of(2024, Month.MARCH, 1);
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(() -> LocalDateTime.now(ArgumentMatchers.any(Clock.class))).thenReturn(fixedLocalDateTime);
            schedulingJobs.createMonthlyBillFacts();
            List<MonthlyBillFacts> monthlyBillFactsList = monthlyBillFactsRepo.findByMonthAndYear(yearMonth);
            assertThat(monthlyBillFactsList).hasSize(size);
        }
    }

}
