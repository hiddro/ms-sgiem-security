package com.sgiem.ms.security.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

    @Entity(name = "rol_credential")
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public class RolCredential implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        @Column(name = "idRol")
        private int idRol;

        @Column(length = 50, name = "titulo")
        private String titulo;

        @ManyToOne
        @JoinColumn(name = "idUser")
        private UserCredential user;
    }
