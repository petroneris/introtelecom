package com.snezana.introtelecom.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "admin", catalog = "intro_telecom")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id", unique = true, nullable = false)
    private Long adminId;

    @Column(name = "apersonal_number", unique = true, nullable = false)
    private String personalNumber;

    @Column(name = "afirst_name", nullable = false)
    private String firstName;

    @Column(name = "alast_name", nullable = false)
    private String lastName;

    @Column(name = "a_email", unique = true, nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phone_number", referencedColumnName = "phone_number")
    private Phone phone;
}
