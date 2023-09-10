package com.snezana.introtelecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "service", catalog = "intro_telecom")
public class PhoneService {

    @Id
    @Column(name = "service_code", unique = true, nullable = false)
    private String serviceCode;

    @Column(name = "service_description", nullable = false)
    private String serviceDescription;

    @Column(name = "service_price", precision = 10 ,scale =2 , nullable = false)
    private BigDecimal servicePrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="phoneService")
    private Set<ServiceDetailRecord> serviceDetailRecords;
}
