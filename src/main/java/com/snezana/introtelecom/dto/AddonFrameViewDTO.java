package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddonFrameViewDTO {

    private Long addfrId;
    private String phoneNumber;
    private String addonCode;
    private String addfrCls;
    private String addfrSms;
    private String addfrInt;
    private String addfrAsm;
    private String addfrIcl;
    private String addfrRmg;
    private LocalDateTime addfrStartDateTime;
    private LocalDateTime addfrEndDateTime;
    private String addfrStatus;
}
