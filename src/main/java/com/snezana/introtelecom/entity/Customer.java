package com.snezana.introtelecom.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "customer", catalog = "intro_telecom")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", unique = true, nullable = false)
    private Long customerId;

    @Column(name = "cpersonal_number", unique = true, nullable = false)
    private String personalNumber;

    @Column(name = "cfirst_name", nullable = false)
    private String firstName;

    @Column(name = "clast_name", nullable = false)
    private String lastName;

    @Column(name = "c_email", unique = true, nullable = false)
    private String email;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToMany(fetch = FetchType.LAZY )
    @JoinTable(
            name = "customer_phone", catalog = "intro_telecom",
            joinColumns = { @JoinColumn(name = "customer_id") },
            inverseJoinColumns = { @JoinColumn(name = "phone_number") }
    )
    private Set<Phone> phones = new HashSet<>();
}
