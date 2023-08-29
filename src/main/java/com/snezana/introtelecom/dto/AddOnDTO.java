package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AddOnDTO {

    private String addonCode;
    private String addonDescription;
    private BigDecimal addonPrice;
}
