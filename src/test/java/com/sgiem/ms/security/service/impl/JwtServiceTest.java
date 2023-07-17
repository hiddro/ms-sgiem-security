package com.sgiem.ms.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    void testGenerateToken() {
        // Arrange
        String userName = "john_doe";

        // Act
        String generatedToken = jwtService.generateToken(userName);

        // Assert
        Assertions.assertNotNull(generatedToken);

        // Puedes verificar que el token es válido decodificándolo y verificando sus claims
        Claims decodedClaims = Jwts.parserBuilder().setSigningKey(jwtService.getSignKey()).build().parseClaimsJws(generatedToken).getBody();
        Assertions.assertEquals(userName, decodedClaims.getSubject());
    }
}