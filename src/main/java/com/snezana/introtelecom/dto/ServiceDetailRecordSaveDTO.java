package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.PhoneService;
import com.snezana.introtelecom.validations.JsonDateTimeDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    private LocalDateTime sdrStartDateTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    private LocalDateTime sdrEndDateTime;

    @NotNull
    private int msgAmount;

    @NotNull
    @Schema(type= "number", format = "double")
    private BigDecimal mbAmount;

}
