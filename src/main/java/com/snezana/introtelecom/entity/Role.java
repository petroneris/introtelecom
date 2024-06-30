package com.snezana.introtelecom.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "role", catalog = "intro_telecom")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id", unique = true, nullable = false)
    private Long roleId;

    @Column(name = "role_type", unique = true, nullable = false)
    private String roleType;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
