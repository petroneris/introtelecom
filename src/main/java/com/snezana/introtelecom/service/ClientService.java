package com.snezana.introtelecom.service;

import com.snezana.introtelecom.dto.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {

    CurrentInfo01ViewDTO getCurrentInfo (Authentication authentication);

    ClientMonthlyBillFactsPrpViewDTO getMonthlyBillFactsByYearAndMonth (Authentication authentication, int year, int month);

    List<? extends ClientMonthlyBillFactsPrpViewDTO> getMonthlyBillFactsFromStartDateToEndDate (Authentication authentication, int startYear, int startMonth, int endYear, int endMonth);

    void changePassword(Authentication authentication, ClientChangePasswordDTO clientChangePasswordDto);
    List<PackagePlanDTO> getAllCustomersPackagePlans();

    List<AddOnDTO> getAllAddOnDetails();

}
