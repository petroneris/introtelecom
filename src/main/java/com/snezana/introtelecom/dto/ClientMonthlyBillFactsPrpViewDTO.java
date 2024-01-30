package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Monthly bill facts for prepaid (PRP01, PRP02) users that client can see
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientMonthlyBillFactsPrpViewDTO {
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String username;
    private String packageName;
    private String packageCode;
}
