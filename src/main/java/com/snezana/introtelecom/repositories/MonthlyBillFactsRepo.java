package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.MonthlyBillFacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface MonthlyBillFactsRepo extends JpaRepository<MonthlyBillFacts, Long> {

    MonthlyBillFacts findByMonthlybillId (Long monthlybillId);

    MonthlyBillFacts findByPhone_PhoneNumberAndYearMonth (String phoneNumber, LocalDate yearMonth);

    List <MonthlyBillFacts> findByPhone_PhoneNumberAndYearMonthStartsWithAndYearMonthEndsWith (String phoneNumber, LocalDate startYearMonth, LocalDate endYearMonth);

    List <MonthlyBillFacts> findByPhone_PhoneNumberAndYearMonthStartsWith (String phoneNumber, LocalDate startYearMonth);

    @Query(
            "SELECT " +
                    "monthlybillFacts "+
                    "FROM MonthlyBillFacts monthlybillFacts "+
                    "WHERE monthlybillFacts.monthlybillId = :monthlybillId "
    )
    Optional<MonthlyBillFacts> findByMonthlybillIdOpt(@Param("monthlybillId") Long monthlybillId);

    @Query(
            "SELECT " +
                    "monthlybillFacts "+
                    "FROM MonthlyBillFacts monthlybillFacts "+
                    "WHERE monthlybillFacts.phone.phoneNumber = :phoneNumber " +
                    "AND monthlybillFacts.yearMonth = :yearMonth "
    )
    Optional<MonthlyBillFacts> findByPhoneNumberAndMonthAndYearOpt(@Param("phoneNumber") String phoneNumber, @Param("yearMonth") LocalDate yearMonth);
}
