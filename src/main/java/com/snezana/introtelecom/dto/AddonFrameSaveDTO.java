package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.entity.AddOn;
import com.snezana.introtelecom.entity.Phone;
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
public class AddonFrameSaveDTO {

    @NotBlank
    private String phone;

    @NotBlank
    private String addOn;

    @NotBlank
    private LocalDateTime addfrStartDateTime;

    @NotBlank
    private LocalDateTime addfrEndDateTime;

}
