package com.sgiem.ms.security.service.impl;

import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.repository.GenericRepositories;
import com.sgiem.ms.security.repository.RolCredentialRepositories;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthRolServiceImplTest {

    @InjectMocks
    private AuthRolServiceImpl authRolServiceImpl;

    @Mock
    private RolCredentialRepositories rolCredentialRepositories;

    @Test
    void testGetRepo(){
        // Arrange

        // Act
        GenericRepositories<RolCredential, Integer> repo = authRolServiceImpl.getRepo();

        // Assert
        Assertions.assertEquals(rolCredentialRepositories, repo);
    }

    @Test
    void testGetAllRolsTitulo() {
        // Arrange
        String titulo = "ADMIN";

        // Configurar la lista de datos de prueba
        List<RolCredential> mockRols = new ArrayList<>();
        RolCredential rol1 = new RolCredential();
        rol1.setIdRol(1);
        rol1.setTitulo("ADMIN");
        rol1.setUser(UserCredential.builder().idUser(1).build());
        mockRols.add(rol1);

        RolCredential rol2 = new RolCredential();
        rol2.setIdRol(2);
        rol2.setTitulo("ADMIN");
        rol2.setUser(UserCredential.builder().idUser(2).build());
        mockRols.add(rol2);


        Mockito.when(rolCredentialRepositories.findAll()).thenReturn(mockRols);

        // Act
        List<RolCredential> filteredRols = authRolServiceImpl.getAllRolsTitulo(titulo);

        // Assert
        Mockito.verify(rolCredentialRepositories, Mockito.times(1)).findAll();
        Assertions.assertEquals(2, filteredRols.size()); // Asegurarse de que la lista filtrada tenga el tamaño esperado
        // Verificar que cada RolCredential en la lista filtrada tenga el título esperado
        for (RolCredential rol : filteredRols) {
            Assertions.assertEquals(titulo, rol.getTitulo(), "Título no coincide para RolCredential con id: " + rol.getIdRol());
        }
    }
}