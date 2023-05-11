package com.sgiem.ms.security.service.impl;

import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.repository.GenericRepositories;
import com.sgiem.ms.security.repository.RolCredentialRepositories;
import com.sgiem.ms.security.service.AuthRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthRolServiceImpl extends CrudServiceImpl<RolCredential, Integer> implements AuthRolService {

    @Autowired
    private RolCredentialRepositories rolCredentialRepositories;

    @Override
    protected GenericRepositories<RolCredential, Integer> getRepo() {
        return rolCredentialRepositories;
    }
}
