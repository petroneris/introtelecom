package com.snezana.introtelecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "addon", catalog = "intro_telecom")
public class AddOn {
    @Id
    @Column(name = "addon_code", unique = true, nullable = false)
    private String addonCode;

    @Column(name = "addon_description", nullable = false)
    private String addonDescription;

    @Column(name = "addon_price", precision = 10 ,scale =2 , nullable = false)
    private BigDecimal addonPrice;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="addOn")
    private Set<AddonFrame> addonFrames;

}
