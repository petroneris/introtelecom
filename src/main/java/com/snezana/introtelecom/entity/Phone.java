package com.snezana.introtelecom.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Data
@Table(name = "phone", catalog = "intro_telecom")
public class Phone {

    @Id
    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_code", nullable=false)
    private PackagePlan packagePlan;

    @Column(name = "phonestart_datetime", nullable = false)
    private LocalDateTime phoneStartDateTime;

    @Column(name = "phone_status", nullable = false)
    private String phoneStatus;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "phone")
    private Set<Admin> admins;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "phone", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user; //User

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "phones")
    private Set<Customer> customers = new HashSet<>();

}
