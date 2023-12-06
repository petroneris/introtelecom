package com.snezana.introtelecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "monthlybill_info", catalog = "intro_telecom")
public class MonthlyBillFacts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monthlybill_id", unique = true, nullable = false)
    private Long monthlybillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number", referencedColumnName = "phone_number")
    private Phone phone;

    @Column(name = "year_month", nullable = false)
    private LocalDate yearMonth;

    @Column(name = "package_price", precision = 10 ,scale = 2 , nullable = false)
    private BigDecimal packagePrice;

    @Column(name = "addcls_price", precision = 10 ,scale = 2 , nullable = false)
    private BigDecimal addclsPrice;

    @Column(name = "addsms_price", precision = 10 ,scale = 2 , nullable = false)
    private BigDecimal addsmsPrice;

    @Column(name = "addint_price", precision = 10 ,scale = 2 , nullable = false)
    private BigDecimal addintPrice;

    @Column(name = "addasm_price", precision = 10 ,scale = 2 , nullable = false)
    private BigDecimal addasmPrice;

    @Column(name = "addicl_price", precision = 10 ,scale = 2 , nullable = false)
    private BigDecimal addiclPrice;

    @Column(name = "addrmg_price", precision = 10 ,scale = 2 , nullable = false)
    private BigDecimal addrmgPrice;

    @Column(name = "monthlybill_totalprice", precision = 10 ,scale = 2 , nullable = false)
    private BigDecimal monthlybillTotalprice;

    @Column(name = "monthlybill_datetime", nullable = false)
    private LocalDateTime monthlybillDateTime;
}
