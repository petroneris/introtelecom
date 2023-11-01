package com.snezana.introtelecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageFrameViewDTO {

    private Long packfrId;
    private String phoneNumber;
    private String packageCode;
    private String packfrCls;
    private String packfrSms;
    private String packfrInt;
    private String packfrAsm;
    private String packfrIcl;
    private String packfrRmg;
    private LocalDateTime packfrStartDateTime;
    private LocalDateTime packfrEndDateTime;
    private String packfrStatus;
}
