package com.sgiem.ms.security.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

@Entity(name = "user_credential")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserCredential implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "idUser")
    private int idUser;


    @Column(name = "names")
    private String names;

    @Column(name = "surenames")
    private String surenames;

    @Column(name = "code")
    private String code;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "idUser", referencedColumnName = "idUser"),
            inverseJoinColumns = @JoinColumn(name = "idRol", referencedColumnName = "idRol"))
    private List<RolCredential> roles = new ArrayList<>();
}
