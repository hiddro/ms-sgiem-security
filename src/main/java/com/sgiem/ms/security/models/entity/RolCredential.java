package com.sgiem.ms.security.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

@Entity(name = "rol_credential")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RolCredential implements Serializable {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Column(name = "idRol")
    private Integer idRol;

    @Column(length = 50, name = "titulo")
    private String titulo;

    @ManyToMany
    @JsonBackReference
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "idRol", referencedColumnName = "idRol"),
            inverseJoinColumns = @JoinColumn(name = "idUser", referencedColumnName = "idUser"))
    private List<UserCredential> usuarios = new ArrayList<>();
}
