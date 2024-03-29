package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Monthly bill facts for postpaid (PST11, PST12, PST13, PST14) users that client can see
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientMonthlyBillFactsPstViewDTO extends ClientMonthlyBillFactsPrpViewDTO{
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String username;
    private String packageName;
    private String packageCode;
    private String month;
    private int year;
    private String packagePrice;
    private String addclsPrice;
    private String addsmsPrice;
    private String addintPrice;
    private String addasmPrice;
    private String addiclPrice;
    private String addrmgPrice;
    private String monthlybillTotalprice;
    private LocalDateTime monthlybillDateTime;

}
