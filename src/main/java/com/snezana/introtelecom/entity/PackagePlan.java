package com.snezana.introtelecom.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "package_plan", catalog = "intro_telecom")
public class PackagePlan {

    @Id
    @Column(name = "package_code", unique = true, nullable = false)
    private String packageCode;

    @Column(name = "package_name", unique = true, nullable = false)
    private String packageName;

    @Column(name = "package_description", nullable = false)
    private String packageDescription;

    @Column(name = "package_price", precision = 10 ,scale =2 , nullable = false)
    private BigDecimal packagePrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="packagePlan")
    private Set<Phone> phones;

}
