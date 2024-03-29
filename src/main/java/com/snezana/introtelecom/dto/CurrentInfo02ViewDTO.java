package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Current info view for prepaid 02 - PRP02 users
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "phoneNumber", "packageCode", "currCls", "currSms", "currInt", "addCls", "addSms", "addInt", "currDateTime" })
public class CurrentInfo02ViewDTO extends CurrentInfo01ViewDTO{

    private String phoneNumber;
    private String packageCode;
    private String currCls;
    private String currSms;
    private String currInt;
    private String addCls;
    private String addSms;
    private String addInt;
    private LocalDateTime currDateTime;
}
