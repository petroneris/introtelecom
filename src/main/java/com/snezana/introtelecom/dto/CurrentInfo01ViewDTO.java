package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.snezana.introtelecom.enums.PackagePlanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/* Current info view for prepaid 01 - PRP01 users */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "phoneNumber", "packageCode", "currCls", "currSms", "addCls", "addSms", "currDateTime" })
public class CurrentInfo01ViewDTO {

    private String phoneNumber;
    private String packageCode;
    private String currCls;
    private String currSms;
    private String addCls;
    private String addSms;
    private LocalDateTime currDateTime;
}
