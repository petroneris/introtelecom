package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.snezana.introtelecom.enums.PackagePlanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "phoneNumber", "firstName", "lastName", "username", "packageName", "packageCode", "currCls", "currSms","addCls", "addSms", "currDateTime" })
public class ClientCurrentInfo01ViewDTO {
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String username;
    private String packageName;
    private String packageCode;
    private String currCls;
    private String currSms;
    private String addCls;
    private String addSms;
    private LocalDateTime currDateTime;
}
