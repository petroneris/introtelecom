package com.snezana.introtelecom.dto;

import com.snezana.introtelecom.entity.AddOn;
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
public class AddonFrameViewDTO {

    private Long addfrId;
    private String phone;
    private String addOn;
    private int addfrCls;
    private int addfrSms;
    private BigDecimal addfrInt;
    private BigDecimal addfrAsm;
    private BigDecimal addfrIcl;
    private BigDecimal addfrRmg;
    private LocalDateTime addfrStartDateTime;
    private LocalDateTime addfrEndDateTime;
    private String addfrStatus;
}
