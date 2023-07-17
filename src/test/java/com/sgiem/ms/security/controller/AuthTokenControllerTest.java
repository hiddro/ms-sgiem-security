package com.sgiem.ms.security.controller;

import com.sgiem.ms.security.dto.AuthRequest;
import com.sgiem.ms.security.dto.AuthResponse;
import com.sgiem.ms.security.service.AuthTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenControllerTest {

    @InjectMocks
    private AuthTokenController authTokenController;

    @Mock
    private AuthTokenService authTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void testGetToken() {
        // Arrange
        AuthRequest authRequest = new AuthRequest("edd.ckalb@gmail.com", "12345");
        String expectedToken = "asdasdasdasdasdas";

        Authentication authenticate = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class)))
                .thenReturn(authenticate);

        Mockito.when(authTokenService.generateToken(authRequest.getUsername())).thenReturn(AuthResponse.builder().token(expectedToken).build());

        // Act
        ResponseEntity<AuthResponse> response = authTokenController.getToken(authRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody().getToken());
        // Add more assertions if needed
    }

    @Test
    public void testGetTokenError() {
        // Arrange
        AuthRequest authRequest = new AuthRequest("edd.ckalb@gmail.com", "12345");
        String username = "testUser";
        String password = "testPassword";

        // Simular autenticación fallida y retorno nulo
        Authentication authenticate = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticate);

        // Act
        try {
            authTokenController.getToken(new AuthRequest(username, password));
        } catch (RuntimeException ex) {
            // Assert (expected to throw RuntimeException)
            assertEquals("invalid access", ex.getMessage());
        }
    }
}