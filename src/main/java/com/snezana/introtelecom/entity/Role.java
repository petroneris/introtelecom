package com.snezana.introtelecom.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
@Table(name = "role", catalog = "intro_telecom")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id", unique = true, nullable = false)
    private Long roleId;

    @Column(name = "role_type", unique = true, nullable = false)
    private String roleType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private Set<User> users;
}
