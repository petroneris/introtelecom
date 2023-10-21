package com.snezana.introtelecom.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SdrAmountAux {

    private int auxDuration;
    private int auxMsgAmount;
    private BigDecimal auxMBamount;
    private BigDecimal auxPrice;
    private String note;
    private String serviceCode;
    private boolean eos;
}
