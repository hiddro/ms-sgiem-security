package com.sgiem.ms.security.service.impl;

import com.example.UserCredentialDto;
import com.sgiem.ms.security.config.kafka.Producer;
import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.repository.GenericRepositories;
import com.sgiem.ms.security.repository.RolCredentialRepositories;
import com.sgiem.ms.security.repository.UserCredentialRepositories;
import com.sgiem.ms.security.service.EmployeesService;
import com.sgiem.ms.security.utils.commons.Commons;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthUserServiceImplTest {

    @InjectMocks
    private AuthUserServiceImpl authUserServiceImpl;

    @Mock
    private UserCredentialRepositories userCredentialRepositories;

    @Mock
    private RolCredentialRepositories rolCredentialRepositories;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmployeesService employeesService;

    @Mock
    private Producer producer;

    @Test
    void testGetRepo() {
        // Arrange

        // Act
        GenericRepositories<UserCredential, Integer> repo = authUserServiceImpl.getRepo();

        // Assert
        Assertions.assertEquals(userCredentialRepositories, repo);
    }

    @Test
    void testSaveUser() {
        // Arrange
        String email = "example@example.com";
        String password = "examplePassword";
        String names = "John";
        String surenames = "Doe";

        UserCredential credential = UserCredential.builder()
                .email(email)
                .password(password)
                .names(names)
                .surenames(surenames)
                .build();

        // Mock para userBD
        Mockito.when(userCredentialRepositories.findByEmail(email)).thenReturn(Optional.empty());

        // Mock para save en userCredentialRepositories
        UserCredential savedUser = UserCredential.builder()
                .idUser(1)
                .email(email)
                .password(password)
                .names(names)
                .surenames(surenames)
                .code("ABC123")
                .state("ACTIVO")
                .build();
        Mockito.when(userCredentialRepositories.save(Mockito.any(UserCredential.class))).thenReturn(savedUser);

        // Mock para save en rolCredentialRepositories
        RolCredential savedRol = RolCredential.builder()
                .idRol(1)
                .titulo("USER")
                .user(savedUser)
                .build();
        Mockito.when(rolCredentialRepositories.save(Mockito.any(RolCredential.class))).thenReturn(savedRol);

        // Act
        com.sgiem.ms.security.dto.UserResponse result = authUserServiceImpl.saveUser(credential);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser.getIdUser(), result.getIdUser());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getNames(), result.getNames());
        assertEquals(savedUser.getSurenames(), result.getSurenames());
        assertNotNull(result.getCode());
        assertEquals(Commons.validateState(savedUser.getState()), result.getState());

        List<com.sgiem.ms.security.dto.RolDetails> expectedRoles = Arrays.asList(com.sgiem.ms.security.dto.RolDetails.builder()
                .idRol(savedRol.getIdRol())
                .titulo(Commons.validateTitulo(savedRol.getTitulo()))
                .idUser(savedRol.getUser().getIdUser())
                .build());
        assertEquals(expectedRoles, result.getRoles());

        // Verificar que los métodos save se llamen con los parámetros correctos
        Mockito.verify(userCredentialRepositories, Mockito.times(1)).findByEmail(email);
        Mockito.verify(userCredentialRepositories, Mockito.times(1)).save(Mockito.any(UserCredential.class));
        Mockito.verify(rolCredentialRepositories, Mockito.times(1)).save(Mockito.any(RolCredential.class));
        Mockito.verify(producer, Mockito.times(1)).sendMessage(Mockito.anyString(), Mockito.any(UserCredentialDto.class));
    }

    @Test
    void testSaveUserExist() {
        // Arrange
        UserCredential existingUser = UserCredential.builder()
                .idUser(1)
                .email("user@example.com")
                .build();

        // Simulamos que ya existe un usuario con el mismo email en la base de datos
        Mockito.when(userCredentialRepositories.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        // Creamos un nuevo usuario con el mismo email para probar que lanza una excepción
        UserCredential newUser = UserCredential.builder()
                .email(existingUser.getEmail())
                .build();

        // Act y Assert
        assertThrows(RuntimeException.class, () -> authUserServiceImpl.saveUser(newUser));
    }

    @Test
    void testAssignRolUser() {
        // Arrange
        String titulo = "admin";
        String code = "JD001";

        UserCredential userCredential = UserCredential.builder()
                .idUser(1)
                .code(code)
                .email("example@example.com")
                .names("John")
                .surenames("Doe")
                .state("ACTIVO")
                .roles(new ArrayList<>())
                .build();

        RolCredential savedRol = RolCredential.builder()
                .idRol(1)
                .titulo(titulo)
                .user(userCredential)
                .build();

        // Mock para userCredentialRepositories.findByCode
        Mockito.when(userCredentialRepositories.findByCode(code)).thenReturn(Optional.of(userCredential));

        // Mock para rolCredentialRepositories.save
        Mockito.when(rolCredentialRepositories.save(Mockito.any(RolCredential.class))).thenReturn(savedRol);

        // Act
        com.sgiem.ms.security.dto.UserResponse result = authUserServiceImpl.assignRolUser(titulo, code);

        // Assert
        assertNotNull(result);
        assertEquals(userCredential.getIdUser(), result.getIdUser());
        assertEquals(userCredential.getEmail(), result.getEmail());
        assertEquals(userCredential.getNames(), result.getNames());
        assertEquals(userCredential.getSurenames(), result.getSurenames());
        assertEquals(userCredential.getCode(), result.getCode());
        assertEquals(Commons.validateState(userCredential.getState()), result.getState());
        assertEquals(Commons.validateRolArray(userCredential), result.getRoles());

        // Verificar que se llamó el método employeesService.addRolEmployees con los parámetros correctos
        Mockito.verify(employeesService, Mockito.times(1)).addRolEmployees(titulo, code);
    }

    @Test
    void resetPass() {
        // Arrange
        String email = "example@example.com";
        String newPassword = "newPassword";

        UserCredential userCredential = UserCredential.builder()
                .idUser(1)
                .email(email)
                .names("John")
                .surenames("Doe")
                .code("JD001")
                .state("ACTIVO")
                .roles(new ArrayList<>())
                .build();

        com.sgiem.ms.security.dto.PasswordRequest passwordRequest = new com.sgiem.ms.security.dto.PasswordRequest(newPassword);

        // Mock para userCredentialRepositories.findByEmail
        Mockito.when(userCredentialRepositories.findByEmail(email)).thenReturn(Optional.of(userCredential));

        // Mock para passwordEncoder.encode
        String encodedPassword = "encodedPassword";
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        // Act
        com.sgiem.ms.security.dto.UserResponse result = authUserServiceImpl.resetPass(email, passwordRequest);

        // Assert
        assertNotNull(result);
        assertEquals(userCredential.getIdUser(), result.getIdUser());
        assertEquals(userCredential.getEmail(), result.getEmail());
        assertEquals(userCredential.getNames(), result.getNames());
        assertEquals(userCredential.getSurenames(), result.getSurenames());
        assertEquals(userCredential.getCode(), result.getCode());
        assertEquals(Commons.validateState(userCredential.getState()), result.getState());
        assertEquals(Commons.validateRolArray(userCredential), result.getRoles());

        // Verificar que se llamó el método userCredentialRepositories.save con el usuario modificado
        Mockito.verify(userCredentialRepositories, Mockito.times(1)).save(userCredential);
    }

    @Test
    void getUserByCode() {
        // Arrange
        String code = "JD001";

        UserCredential userCredential = UserCredential.builder()
                .idUser(1)
                .email("example@example.com")
                .names("John")
                .surenames("Doe")
                .code(code)
                .state("ACTIVO")
                .roles(new ArrayList<>())
                .build();

        // Mock para userCredentialRepositories.findByCode
        Mockito.when(userCredentialRepositories.findByCode(code)).thenReturn(Optional.of(userCredential));

        // Act
        com.sgiem.ms.security.dto.UserResponse result = authUserServiceImpl.getUserByCode(code);

        // Assert
        assertNotNull(result);
        assertEquals(userCredential.getIdUser(), result.getIdUser());
        assertEquals(userCredential.getEmail(), result.getEmail());
        assertEquals(userCredential.getNames(), result.getNames());
        assertEquals(userCredential.getSurenames(), result.getSurenames());
        assertEquals(userCredential.getCode(), result.getCode());
        assertEquals(Commons.validateState(userCredential.getState()), result.getState());
        assertEquals(Commons.validateRolArray(userCredential), result.getRoles());
    }

    @Test
    void getAllUsersTitulo() {
        // Arrange
        String state = "ACTIVO";

        // Datos de ejemplo para simular usuarios en la base de datos
        List<UserCredential> users = Arrays.asList(
                UserCredential.builder().state("ACTIVO").build(),
                UserCredential.builder().state("INACTIVO").build(),
                UserCredential.builder().state("ACTIVO").build(),
                UserCredential.builder().state("PENDIENTE").build(),
                UserCredential.builder().state("ACTIVO").build()
        );

        // Mock para userCredentialRepositories.findAll
        Mockito.when(userCredentialRepositories.findAll()).thenReturn(users);

        // Act
        List<UserCredential> result = authUserServiceImpl.getAllUsersTitulo(state);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size()); // Esperamos obtener 3 usuarios con estado "ACTIVO"
    }

    @Test
    void updateStateUser() {
        // Arrange
        String code = "JD001";
        String initialState = "PENDIENTE";
        String updatedState = "ACTIVO";

        // Creamos un objeto de ejemplo para simular el usuario en la base de datos
        UserCredential user = UserCredential.builder()
                .code(code)
                .state(initialState)
                .roles(new ArrayList<>())
                .build();

        // Mock para userCredentialRepositories.findByCode
        Mockito.when(userCredentialRepositories.findByCode(code)).thenReturn(Optional.of(user));

        // Act
        com.sgiem.ms.security.dto.UserResponse result = authUserServiceImpl.updateStateUser(code);

        // Assert
        assertNotNull(result);
        assertEquals(updatedState, result.getState().getValue()); // Esperamos que el estado haya sido actualizado correctamente
    }
}