package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.MonthlyBillFacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

public interface MonthlyBillFactsRepo extends JpaRepository<MonthlyBillFacts, Long> {

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

    @Query(
            "SELECT " +
                    "monthlybillFacts "+
                    "FROM MonthlyBillFacts monthlybillFacts "+
                    "WHERE monthlybillFacts.phone.phoneNumber = :phoneNumber " +
                    "AND monthlybillFacts.yearMonth >= :startYearMonth " +
                    "AND monthlybillFacts.yearMonth <= :endYearMonth "
    )
    List <MonthlyBillFacts> findByPhone_PhoneNumberAndYearMonthStartsWithAndYearMonthEndsWith (@Param("phoneNumber") String phoneNumber, @Param("startYearMonth") LocalDate startYearMonth, @Param("endYearMonth") LocalDate endYearMonth);

    @Query(
            "SELECT " +
                    "monthlybillFacts "+
                    "FROM MonthlyBillFacts monthlybillFacts "+
                    "WHERE monthlybillFacts.yearMonth = :yearMonth "
    )
    List<MonthlyBillFacts> findByMonthAndYear(@Param("yearMonth") LocalDate yearMonth);
}
