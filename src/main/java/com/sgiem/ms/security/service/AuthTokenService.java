package com.sgiem.ms.security.service;

import com.sgiem.ms.security.dto.AuthResponse;

public interface AuthTokenService {
    public AuthResponse generateToken(String username);
}
