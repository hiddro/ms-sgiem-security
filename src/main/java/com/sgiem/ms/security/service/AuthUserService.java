package com.sgiem.ms.security.service;

import com.sgiem.ms.security.dto.PasswordRequest;
import com.sgiem.ms.security.dto.UserResponse;
import com.sgiem.ms.security.models.entity.UserCredential;

import java.util.List;

public interface AuthUserService extends CrudService<UserCredential, Integer>{

    public UserResponse saveUser(UserCredential credential);

    public UserResponse assignRolUser(String titulo, String code);

    public UserResponse resetPass(String email, PasswordRequest passwordRequest);

    public UserResponse getUserByCode(String code);

    public List<UserCredential> getAllUsersTitulo(String state);

    public UserResponse updateStateUser(String code);
}
