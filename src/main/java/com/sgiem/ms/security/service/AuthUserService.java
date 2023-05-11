package com.sgiem.ms.security.service;

import com.sgiem.ms.security.dto.UserResponse;
import com.sgiem.ms.security.models.entity.UserCredential;

public interface AuthUserService extends CrudService<UserCredential, Integer>{

    public UserResponse saveUser(UserCredential credential);
}
