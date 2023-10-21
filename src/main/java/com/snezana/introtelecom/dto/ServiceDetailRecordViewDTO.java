package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.PhoneService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailRecordViewDTO {

    private Long sdrId;
    private String phone;
    private String phoneService;
    private String calledNumber;
    private LocalDateTime sdrStartDateTime;
    private LocalDateTime sdrEndDateTime;
    private int duration;
    private int msgAmount;
    private BigDecimal mbAmount;
    private String sdrNote;

}
