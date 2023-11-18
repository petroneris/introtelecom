package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneViewDTO {

    private String phoneNumber;
    private String packageCode;
    private LocalDateTime phoneStartDateTime;
    private String phoneStatus;
    private String note;
}
