package com.sgiem.ms.security.service;

import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;

import java.util.List;

public interface AuthService {

    public String saveUser(UserCredential credential);

    public String generateToken(String username);

    public RolCredential createRol(RolCredential rol);

    public List<RolCredential> getAllRoles();

    public List<UserCredential> getAllUsers();

    public void validateToken(String token);
}
