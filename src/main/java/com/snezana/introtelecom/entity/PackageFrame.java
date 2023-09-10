package com.snezana.introtelecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "package_frame", catalog = "intro_telecom")
public class PackageFrame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packfr_id", unique = true, nullable = false)
    private Long packfrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number", referencedColumnName = "phone_number")
    private Phone phone;

    @Column(name = "packfr_cls", nullable = false)
    private int packfrCls;

    @Column(name = "packfr_sms", nullable = false)
    private int packfrSms;

    @Column(name = "packfr_int", precision = 8 ,scale =2 , nullable = false)
    private BigDecimal packfrInt;

    @Column(name = "packfr_asm", precision = 8 ,scale =2 , nullable = false)
    private BigDecimal packfrAsm;

    @Column(name = "packfr_icl", precision = 10 ,scale =2 , nullable = false)
    private BigDecimal packfrIcl;

    @Column(name = "packfr_rmg", precision = 10 ,scale =2 , nullable = false)
    private BigDecimal packfrRmg;

    @Column(name = "packfr_startdatetime", nullable = false)
    private LocalDateTime packfrStartDateTime;

    @Column(name = "packfr_enddatetime", nullable = false)
    private LocalDateTime packfrEndDateTime;

    @Column(name = "packfr_status", nullable = false)
    private String packfrStatus;
}
