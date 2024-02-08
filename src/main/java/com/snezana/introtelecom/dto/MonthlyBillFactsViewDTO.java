package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyBillFactsViewDTO {
    private Long monthlybillId;
    private String phoneNumber;
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
