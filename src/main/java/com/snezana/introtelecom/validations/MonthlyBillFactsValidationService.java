package com.snezana.introtelecom.validations;

import com.snezana.introtelecom.entity.MonthlyBillFacts;
import com.snezana.introtelecom.entity.ServiceDetailRecord;
import com.snezana.introtelecom.exceptions.IllegalItemFieldException;
import com.snezana.introtelecom.exceptions.ItemNotFoundException;
import com.snezana.introtelecom.exceptions.RestAPIErrorMessage;
import com.snezana.introtelecom.repositories.MonthlyBillFactsRepo;
import com.snezana.introtelecom.repositories.ServiceDetailRecordRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;

@Service
@RequiredArgsConstructor
public class MonthlyBillFactsValidationService {

    private final MonthlyBillFactsRepo monthlyBillFactsRepo;

    public MonthlyBillFacts returnTheMonthlyBillFactsByIdIfExists (Long monthlybillId){
        return monthlyBillFactsRepo.findByMonthlybillIdOpt(monthlybillId)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Monthly Bill with this Id is not found."));
    }

    public MonthlyBillFacts returnTheMonthlyBillFactsByYearMonthIfExists (String phoneNumber, LocalDate yearMonth){
        return monthlyBillFactsRepo.findByPhoneNumberAndMonthAndYearOpt(phoneNumber, yearMonth)
                .orElseThrow(() -> new ItemNotFoundException(RestAPIErrorMessage.ITEM_NOT_FOUND, "Monthly Bill with this year and month is not found."));
    }

    public void checkYearAndMonth (int year, int month){
        if (!(year> (LocalDate.now().getYear() - 3) && year <= LocalDate.now().getYear())){
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "Year must be between 2021 and " +LocalDate.now().getYear());
        }
        if (!(month >0 && month <= 12)){
            throw new IllegalItemFieldException(RestAPIErrorMessage.WRONG_ITEM, "Month must be between 1 and 12");
        }
    }

    public void controlTheStartDateIsLessThanEndDate (LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate) || startDate.isEqual(endDate)){
            throw new IllegalItemFieldException(RestAPIErrorMessage.INVALID_STARTDATE_OR_ENDDATE, "StartDate must be less than EndDate!");
        }
    }

    public void controlTheTimeForScheduling (){
        LocalDateTime nowDateTime = LocalDateTime.now();
        Month currentMonth = nowDateTime.getMonth();
        int currentYear = nowDateTime.getYear();
        LocalDateTime startDateTime = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(currentYear, currentMonth, 1, 12, 0, 0, 0);
//        LocalDateTime startDateTime = LocalDateTime.of(currentYear, currentMonth, 21, 18, 0, 0, 0);
//        LocalDateTime endDateTime = LocalDateTime.of(currentYear, currentMonth, 21, 20, 0, 0, 0);
        if (nowDateTime.isAfter(startDateTime) && nowDateTime.isBefore(endDateTime)){
            throw new IllegalItemFieldException(RestAPIErrorMessage.INVALID_DATETIME_INTERVAL, "Data are not available until 12 o'clock today!");
        }
    }
}
