package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.CurrentInfo01ViewDTO;
import com.snezana.introtelecom.dto.MonthlyBillFactsViewDTO;

import java.util.List;

public interface CurrentInfoMonthlyBillFactsService {

    CurrentInfo01ViewDTO getCurrentInfoByPhone (String phoneNumber);

    MonthlyBillFactsViewDTO getMonthlyBillFactsById (Long monthlybillId);

    MonthlyBillFactsViewDTO getMonthlyBillFactsByPhoneAndYearAndMonth (String phoneNumber, int year, int month);

    List<MonthlyBillFactsViewDTO> getMonthlyBillFactsByPhoneFromStartDateToEndDate (String phoneNumber, int startYear, int startMonth, int endYear, int endMonth);

}
