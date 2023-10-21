package com.snezana.introtelecom.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultingAmountOfService {

    private int amountCls;
    private int amountSms;
    private BigDecimal amountInt;
    private BigDecimal amountAsm;
    private BigDecimal amountIcl;
    private BigDecimal amountRmg;

}
