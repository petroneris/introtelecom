package com.snezana.introtelecom.dto;

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
public class PackageFrameSaveDTO {

    @NotBlank
    private String phone;

    @NotBlank
    private LocalDateTime packfrStartDateTime;

    @NotBlank
    private LocalDateTime packfrEndDateTime;

}
