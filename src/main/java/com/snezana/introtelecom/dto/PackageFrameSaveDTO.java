package com.snezana.introtelecom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.snezana.introtelecom.validation.JsonDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Currently not in use, because of automatic generation of package frames at the beginning of the month
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageFrameSaveDTO {

    @NotBlank
    private String phoneNumber;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    private LocalDateTime packfrStartDateTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = JsonDateTimeDeserializer.class)
    private LocalDateTime packfrEndDateTime;



}
