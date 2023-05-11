package com.sgiem.ms.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepositories<T, ID> extends JpaRepository<T, ID> {
}
