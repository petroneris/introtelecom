package com.snezana.introtelecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "addon_frame", catalog = "intro_telecom")
public class AddonFrame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addfr_id", unique = true, nullable = false)
    private Long addfrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number", referencedColumnName = "phone_number")
    private Phone phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_code", nullable=false)
    private AddOn addOn;

    @Column(name = "addfr_cls", nullable = false)
    private int addfrCls;

    @Column(name = "addfr_sms", nullable = false)
    private int addfrSms;

    @Column(name = "addfr_int", precision = 8 ,scale =2 , nullable = false)
    private BigDecimal addfrInt;

    @Column(name = "addfr_asm", precision = 8 ,scale =2 , nullable = false)
    private BigDecimal addfrAsm;

    @Column(name = "addfr_icl", precision = 10 ,scale =2 , nullable = false)
    private BigDecimal addfrIcl;

    @Column(name = "addfr_rmg", precision = 10 ,scale =2 , nullable = false)
    private BigDecimal addfrRmg;

    @Column(name = "addfr_startdatetime", nullable = false)
    private LocalDateTime addfrStartDateTime;

    @Column(name = "addfr_enddatetime", nullable = false)
    private LocalDateTime addfrEndDateTime;

    @Column(name = "addfr_status", nullable = false)
    private String addfrStatus;
}
