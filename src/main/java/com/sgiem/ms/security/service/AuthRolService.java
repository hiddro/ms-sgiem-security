package com.sgiem.ms.security.service;

import com.sgiem.ms.security.models.entity.RolCredential;

import java.util.List;

public interface AuthRolService extends CrudService<RolCredential, Integer>{

    public List<RolCredential> getAllRolsTitulo(String titulo);
}
