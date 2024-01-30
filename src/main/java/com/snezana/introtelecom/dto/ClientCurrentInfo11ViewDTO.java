package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Current info view for postpaid 11 - PST11 users that client can see
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "phoneNumber", "firstName", "lastName", "username", "packageName", "packageCode", "currCls", "currSms", "currInt", "currIcl", "currRmg", "addCls", "addSms", "addInt", "addIcl", "addRmg", "currDateTime" })
public class ClientCurrentInfo11ViewDTO extends CurrentInfo01ViewDTO{
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String username;
    private String packageName;
    private String packageCode;
    private String currCls;
    private String currSms;
    private String currInt;
    private String currIcl;
    private String currRmg;
    private String addCls;
    private String addSms;
    private String addInt;
    private String addIcl;
    private String addRmg;
    private LocalDateTime currDateTime;
}
