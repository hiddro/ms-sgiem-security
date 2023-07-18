package com.sgiem.ms.security.controller;

import com.sgiem.ms.security.api.v1.TokenApi;
import com.sgiem.ms.security.dto.AuthRequest;
import com.sgiem.ms.security.dto.AuthResponse;
import com.sgiem.ms.security.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/token")
public class AuthTokenController implements TokenApi {

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<AuthResponse> getToken(AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return new ResponseEntity<>(authTokenService.generateToken(authRequest.getUsername()), HttpStatus.OK);
        } else {
            throw new RuntimeException("invalid access");
        }
    }
}
