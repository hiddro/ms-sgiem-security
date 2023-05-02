package com.sgiem.ms.security.repository;

import com.sgiem.ms.security.models.entity.RolCredential;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RolCredentialRepository extends JpaRepository<RolCredential, Integer> {
}