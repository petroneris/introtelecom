package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneServiceDTO {

    private String serviceCode;
    private String serviceDescription;
    private String servicePrice;
}
