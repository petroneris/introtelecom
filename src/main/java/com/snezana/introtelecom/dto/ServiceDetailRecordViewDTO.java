package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailRecordViewDTO {

    private Long sdrId;
    private String phoneNumber;
    private String serviceCode;
    private String calledNumber;
    private LocalDateTime sdrStartDateTime;
    private LocalDateTime sdrEndDateTime;
    private int duration;
    private int msgAmount;
    private BigDecimal mbAmount;
    private String sdrNote;

}
