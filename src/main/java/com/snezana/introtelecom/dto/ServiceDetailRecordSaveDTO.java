package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.PhoneService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailRecordSaveDTO {

    @NotBlank
    private String phone;

    @NotBlank
    private String phoneService;

    @NotBlank
    private String calledNumber;

    @NotBlank
    private LocalDateTime sdrStartDateTime;

    @NotBlank
    private LocalDateTime sdrEndDateTime;

    @NotBlank
    private int msgAmount;

    @NotBlank
    private BigDecimal mbAmount;

}
