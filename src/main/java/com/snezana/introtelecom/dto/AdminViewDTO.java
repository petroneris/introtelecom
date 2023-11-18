package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminViewDTO {

    private Long adminId;
    private String personalNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}

