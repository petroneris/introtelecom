package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "phoneNumber", "firstName", "lastName", "username", "packageName", "packageCode", "currCls", "currSms", "currInt", "currAsm", "currIcl", "currRmg", "addCls", "addSms", "addInt", "addAsm", "addIcl", "addRmg", "currDateTime" })
public class ClientCurrentInfo1234ViewDTO extends ClientCurrentInfo01ViewDTO{
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String username;
    private String packageName;
    private String packageCode;
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
