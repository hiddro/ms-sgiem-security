package com.sgiem.ms.security.repository;

import com.sgiem.ms.security.models.entity.UserCredential;

import java.util.Optional;

public interface UserCredentialRepositories extends GenericRepositories<UserCredential, Integer> {
    Optional<UserCredential> findByEmail(String username);

    Optional<UserCredential> findByCode(String code);
}