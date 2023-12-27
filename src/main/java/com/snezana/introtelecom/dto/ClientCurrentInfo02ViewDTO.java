package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "phoneNumber", "firstName", "lastName", "username", "packageName", "packageCode", "currCls", "currSms", "currInt", "addCls", "addSms", "addInt", "currDateTime" })
public class ClientCurrentInfo02ViewDTO extends ClientCurrentInfo01ViewDTO{
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String username;
    private String packageName;
    private String packageCode;
    private String currCls;
    private String currSms;
    private String currInt;
    private String addCls;
    private String addSms;
    private String addInt;
    private LocalDateTime currDateTime;
}
