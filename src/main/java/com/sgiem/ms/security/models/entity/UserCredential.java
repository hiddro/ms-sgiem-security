package com.sgiem.ms.security.models.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.*;

@Entity(name = "user_credential")
@Getter
@Setter
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

    @Column(name = "state")
    private String state;

    @Column(name = "createTime")
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "es_PE", timezone = "America/Lima")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "updateTime")
    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "es_PE", timezone = "America/Lima")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<RolCredential> roles;
}
