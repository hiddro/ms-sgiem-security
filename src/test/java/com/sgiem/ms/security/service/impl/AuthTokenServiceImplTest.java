package com.sgiem.ms.security.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceImplTest {

    @InjectMocks
    private AuthTokenServiceImpl authTokenService;

    @Mock
    private JwtService jwtService;

    @Test
    void testGenerateToken() {
        // Arrange
        String username = "john_doe";
        String mockToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImpvaG5fZG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.xiESd5Czv2sFnzl-dZ3b3rkV6cJ7t2mMtvQvUVL4fyk";

        Mockito.when(jwtService.generateToken(username)).thenReturn(mockToken);

        // Act
        com.sgiem.ms.security.dto.AuthResponse authResponse = authTokenService.generateToken(username);

        // Assert
        Assertions.assertNotNull(authResponse);
        Assertions.assertEquals(mockToken, authResponse.getToken());
    }
}