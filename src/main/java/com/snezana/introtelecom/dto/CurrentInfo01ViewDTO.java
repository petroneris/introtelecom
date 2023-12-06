package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.enums.PackagePlanType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentInfo01ViewDTO {

    private String phoneNumber;
    private PackagePlanType packagePlan;
    private String currCls;
    private String currSms;
    private String addCls;
    private String addSms;
    private LocalDateTime currDateTime;
}
