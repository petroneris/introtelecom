package com.snezana.introtelecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "sdr", catalog = "intro_telecom")
public class ServiceDetailRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sdr_id", unique = true, nullable = false)
    private Long sdrId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number", referencedColumnName = "phone_number")
    private Phone phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_code", nullable=false)
    private PhoneService phoneService;

    @Column(name = "called_number", nullable = false)
    private String calledNumber;

    @Column(name = "sdr_startdatetime", nullable = false)
    private LocalDateTime sdrStartDateTime;

    @Column(name = "sdr_enddatetime", nullable = false)
    private LocalDateTime sdrEndDateTime;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "msg_amount", nullable = false)
    private int msgAmount;

    @Column(name = "mb_amount", precision = 8 ,scale =2 , nullable = false)
    private BigDecimal mbAmount;

    @Column(name = "sdr_note", nullable = false)
    private String sdrNote;
}
