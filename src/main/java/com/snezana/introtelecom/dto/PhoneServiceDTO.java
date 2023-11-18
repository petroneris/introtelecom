package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PhoneServiceDTO {

    private String serviceCode;
    private String serviceDescription;
    private String servicePrice;
}
