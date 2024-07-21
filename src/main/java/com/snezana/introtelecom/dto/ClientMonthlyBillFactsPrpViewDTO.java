package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * There are no monthly bills for prepaid (01, 02) - PRP01, PRP02 users
 * they can only get the following info:
 * "packageCode": "01  -> THERE IS NO MONTHLY BILL FOR PREPAID PHONE PACKAGES."
 * "packageCode": "02  -> THERE IS NO MONTHLY BILL FOR PREPAID PHONE PACKAGES."
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
