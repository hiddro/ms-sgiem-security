package com.sgiem.ms.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    void testGenerateToken() {
        // Arrange
        String userName = "john_doe";

        // Establecer el valor de la propiedad 'SECRET' en el controlador usando ReflectionTestUtils
        ReflectionTestUtils.setField(jwtService, "SECRET", "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437");

        // Act
        String generatedToken = jwtService.generateToken(userName);

        // Assert
        Assertions.assertNotNull(generatedToken);

        // Puedes verificar que el token es válido decodificándolo y verificando sus claims
        Claims decodedClaims = Jwts.parserBuilder().setSigningKey(jwtService.getSignKey()).build().parseClaimsJws(generatedToken).getBody();
        Assertions.assertEquals(userName, decodedClaims.getSubject());
    }
}