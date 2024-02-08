package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PhoneServiceDTO {

    private String serviceCode;
    private String serviceDescription;
    private String servicePrice;
}
