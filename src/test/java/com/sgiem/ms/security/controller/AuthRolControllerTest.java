package com.sgiem.ms.security.controller;

import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.service.AuthRolService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthRolControllerTest {

    @InjectMocks
    private AuthRolController authRolController;

    @Mock
    private AuthRolService authRolService;

    @Test
    void getListRols() {
        List<RolCredential> mockRols = new ArrayList<>();
        RolCredential rol1 = new RolCredential();
        rol1.setIdRol(1);
        rol1.setTitulo("ADMIN");
        rol1.setUser(UserCredential.builder().idUser(1).build());
        mockRols.add(rol1);

        RolCredential rol2 = new RolCredential();
        rol2.setIdRol(2);
        rol2.setTitulo("USER");
        rol2.setUser(UserCredential.builder().idUser(2).build());
        mockRols.add(rol2);

        Mockito.when(authRolService.findAll()).thenReturn(mockRols);

        // Act
        ResponseEntity<List<com.sgiem.ms.security.dto.RolResponse>> responseEntity = authRolController.listRols();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<com.sgiem.ms.security.dto.RolResponse> rolResponses = responseEntity.getBody();
        Assertions.assertNotNull(rolResponses);
        Assertions.assertEquals(2, rolResponses.size());

        com.sgiem.ms.security.dto.RolResponse firstRolResponse = rolResponses.get(0);
        Assertions.assertEquals(1, firstRolResponse.getIdRol());
        Assertions.assertEquals("ADMIN", firstRolResponse.getTitulo().getValue());
        Assertions.assertEquals(1, firstRolResponse.getIdUser());

        com.sgiem.ms.security.dto.RolResponse secondRolResponse = rolResponses.get(1);
        Assertions.assertEquals(2, secondRolResponse.getIdRol());
        Assertions.assertEquals("USER", secondRolResponse.getTitulo().getValue());
        Assertions.assertEquals(2, secondRolResponse.getIdUser());
    }

    @Test
    void getListRolsTitulo() {
        String tituloToSearch = "Admin";

        List<RolCredential> mockRols = new ArrayList<>();
        RolCredential rol1 = new RolCredential();
        rol1.setIdRol(1);
        rol1.setTitulo("ADMIN");
        rol1.setUser(UserCredential.builder().idUser(1).build());
        mockRols.add(rol1);

        Mockito.when(authRolService.getAllRolsTitulo(tituloToSearch)).thenReturn(mockRols);

        // Act
        ResponseEntity<List<com.sgiem.ms.security.dto.RolResponse>> responseEntity = authRolController.listRolsTitulo(tituloToSearch);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<com.sgiem.ms.security.dto.RolResponse> rolResponses = responseEntity.getBody();
        Assertions.assertNotNull(rolResponses);
        Assertions.assertEquals(1, rolResponses.size());

        com.sgiem.ms.security.dto.RolResponse firstRolResponse = rolResponses.get(0);
        Assertions.assertEquals(1, firstRolResponse.getIdRol());
        Assertions.assertEquals("ADMIN", firstRolResponse.getTitulo().getValue());
        Assertions.assertEquals(1, firstRolResponse.getIdUser());
    }
}