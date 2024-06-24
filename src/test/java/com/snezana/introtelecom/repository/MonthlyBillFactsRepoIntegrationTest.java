package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.MonthlyBillFacts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MonthlyBillFactsRepoIntegrationTest {

    @Autowired
    private MonthlyBillFactsRepo monthlyBillFactsRepo;

    @Test
    void findMonthlyBillFactsOpt_byId_whenIsNotEmpty(){
        Long id = 1L;
        String phoneNumber = "0769317426";
        String localDateStr = "2023-12-01";
        Optional<MonthlyBillFacts> found = monthlyBillFactsRepo.findByMonthlybillIdOpt(id);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(MonthlyBillFacts.class)
                .hasValueSatisfying(monthlyBillFacts-> {
                    assertThat(monthlyBillFacts.getMonthlybillId()).isEqualTo(1L);
                    assertThat(monthlyBillFacts.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(monthlyBillFacts.getYearMonth()).isEqualTo(localDateStr);
                });
    }

    @Test
    void findMonthlyBillFactsOpt_byId_whenIsEmpty(){
        Long id = 200L;
        Optional<MonthlyBillFacts> found = monthlyBillFactsRepo.findByMonthlybillIdOpt(id);
        assertThat(found).isEmpty();
    }

    @Test
    void findMonthlyBillFactsOpt_byPhoneNumber_yearMonth_whenIsNotEmpty(){
        String phoneNumber = "0769317426";
        String localDateStr = "2023-12-01";
        LocalDate yearMonth = LocalDate.of(2023, 12, 1);
        Optional<MonthlyBillFacts> found = monthlyBillFactsRepo.findByPhoneNumberAndMonthAndYearOpt(phoneNumber, yearMonth);
        assertThat(found)
                .isNotEmpty()
                .containsInstanceOf(MonthlyBillFacts.class)
                .hasValueSatisfying(monthlyBillFacts-> {
                    assertThat(monthlyBillFacts.getPhone().getPhoneNumber()).isEqualTo(phoneNumber);
                    assertThat(monthlyBillFacts.getYearMonth()).isEqualTo(localDateStr);
                });
    }

    @Test
    void findMonthlyBillFactsOpt_byPhoneNumber_yearMonth_whenIsEmpty(){
        String phoneNumber = "UNKNOWN";
        LocalDate yearMonth = LocalDate.of(2023, 12, 1);
        Optional<MonthlyBillFacts> found = monthlyBillFactsRepo.findByPhoneNumberAndMonthAndYearOpt(phoneNumber, yearMonth);
        assertThat(found).isEmpty();
    }

    @Test
    void findMonthlyBillFactsList_byPhoneNumber_startYearMonth_endYearMonth(){
        int listSize = 3;
        String phoneNumber = "0769317426";
        LocalDate startYearMonth = LocalDate.of(2023, 12, 1);
        LocalDate endYearMonth = LocalDate.of(2024, 2, 1);
        List<MonthlyBillFacts> foundList = monthlyBillFactsRepo.findByPhone_PhoneNumberAndYearMonthStartsWithAndYearMonthEndsWith (phoneNumber, startYearMonth, endYearMonth);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(monthlyBillFacts -> monthlyBillFacts.getPhone().getPhoneNumber().equals(phoneNumber)
                        && ((monthlyBillFacts.getYearMonth().isEqual(startYearMonth)|| monthlyBillFacts.getYearMonth().isAfter(startYearMonth)) && (monthlyBillFacts.getYearMonth().isEqual(endYearMonth)|| monthlyBillFacts.getYearMonth().isBefore(endYearMonth))))
                .hasSize(listSize);
    }

    @Test
    void findMonthlyBillFactsList_byMonthAndYear(){
        int listSize = 1;
        String phoneNumber = "0769317426";
        LocalDate yearMonth = LocalDate.of(2024, Month.JANUARY, 1);
        List<MonthlyBillFacts> foundList = monthlyBillFactsRepo.findByMonthAndYear (yearMonth);
        assertThat(foundList)
                .isNotEmpty()
                .filteredOn(monthlyBillFacts -> monthlyBillFacts.getPhone().getPhoneNumber().equals(phoneNumber)
                        && ((monthlyBillFacts.getYearMonth().isEqual(yearMonth))))
                .hasSize(listSize);
    }

}
