package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    ClientCurrentInfo01ViewDTO getCurrentInfo (Authentication authentication);

    ClientMonthlyBillFactsPrpViewDTO getMonthlyBillFactsByYearAndMonth (Authentication authentication, int year, int month);

    List<? extends ClientMonthlyBillFactsPrpViewDTO> getMonthlyBillFactsFromStartDateToEndDate (Authentication authentication, int startYear, int startMonth, int endYear, int endMonth);

    List<PackagePlanDTO> getAllCustomersPackagePlans();

    List<AddOnDTO> getAllAddOns();

}
