package com.sgiem.ms.security.service.impl;

import com.sgiem.ms.security.dto.AuthResponse;
import com.sgiem.ms.security.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

    @Autowired
    private JwtService jwtService;

    @Override
    public AuthResponse generateToken(String username) {

        return AuthResponse.builder()
                .token(jwtService.generateToken(username))
                .build();
    }
}
