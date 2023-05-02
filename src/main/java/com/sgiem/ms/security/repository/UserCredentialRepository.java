package com.sgiem.ms.security.repository;

import com.sgiem.ms.security.models.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository  extends JpaRepository<UserCredential, Integer> {
    Optional<UserCredential> findByEmail(String username);
}