package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.entity.Phone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageFrameViewDTO {

    private Long packfrId;
    private String phone;
    private int packfrCls;
    private int packfrSms;
    private BigDecimal packfrInt;
    private BigDecimal packfrAsm;
    private BigDecimal packfrIcl;
    private BigDecimal packfrRmg;
    private LocalDateTime packfrStartDateTime;
    private LocalDateTime packfrEndDateTime;
    private String packfrStatus;
}
