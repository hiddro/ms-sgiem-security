package com.sgiem.ms.security.repository;

import com.sgiem.ms.security.models.entity.RolCredential;

import java.util.Optional;


public interface RolCredentialRepositories extends GenericRepositories<RolCredential, Integer> {

    Optional<RolCredential> findByTitulo(String titulo);
}