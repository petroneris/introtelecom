package com.snezana.introtelecom.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_data", catalog = "intro_telecom")
public class User {
    @Id
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private Role role;

    @Column(name = "user_status", nullable = false)
    private String userStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "phone_number")
    private Phone phone; //Phone

}
