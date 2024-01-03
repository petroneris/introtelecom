package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.snezana.introtelecom.enums.PackagePlanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/* Current info view for postpaid (12, 13, 14) - PST12, PST13, PST14 users */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "phoneNumber", "packagePlan", "currCls", "currSms", "currInt", "currAsm", "currIcl", "currRmg", "addCls", "addSms", "addInt", "addAsm", "addIcl", "addRmg", "currDateTime" })
public class CurrentInfo1234ViewDTO extends CurrentInfo01ViewDTO{

    private String phoneNumber;
    private PackagePlanType packagePlan;
    private String currCls;
    private String currSms;
    private String currInt;
    private String currAsm;
    private String currIcl;
    private String currRmg;
    private String addCls;
    private String addSms;
    private String addInt;
    private String addAsm;
    private String addIcl;
    private String addRmg;
    private LocalDateTime currDateTime;

}
