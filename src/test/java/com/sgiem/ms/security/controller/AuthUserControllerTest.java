package com.sgiem.ms.security.controller;

import com.google.gson.Gson;
import com.sgiem.ms.security.dto.UserResponse;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.service.AuthUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthUserControllerTest {

    @InjectMocks
    private AuthUserController authUserController;

    @Mock
    private AuthUserService authUserService;

    @Test
    void testListUsers() {
        List<UserCredential> mockUsers = new ArrayList<>();
        UserCredential user1 = new UserCredential();
        user1.setIdUser(1);
        user1.setNames("John");
        user1.setSurenames("Doe");
        user1.setCode("JD001");
        user1.setEmail("john.doe@example.com");
        user1.setState("ACTIVO");
        user1.setRoles(new ArrayList<>());
        mockUsers.add(user1);

        UserCredential user2 = new UserCredential();
        user2.setIdUser(2);
        user2.setNames("Jane");
        user2.setSurenames("Smith");
        user2.setCode("JS002");
        user2.setEmail("jane.smith@example.com");
        user2.setState("INACTIVO");
        user2.setRoles(new ArrayList<>());
        mockUsers.add(user2);

        Mockito.when(authUserService.findAll()).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<com.sgiem.ms.security.dto.UserResponse>> responseEntity = authUserController.listUsers();

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<com.sgiem.ms.security.dto.UserResponse> userResponses = responseEntity.getBody();
        Assertions.assertNotNull(userResponses);
        Assertions.assertEquals(2, userResponses.size());

        com.sgiem.ms.security.dto.UserResponse firstUserResponse = userResponses.get(0);
        Assertions.assertEquals(1, firstUserResponse.getIdUser());
        Assertions.assertEquals("John", firstUserResponse.getNames());
        Assertions.assertEquals("Doe", firstUserResponse.getSurenames());
        Assertions.assertEquals("JD001", firstUserResponse.getCode());
        Assertions.assertEquals("john.doe@example.com", firstUserResponse.getEmail());
        Assertions.assertEquals("ACTIVO", firstUserResponse.getState().getValue());
        // Assert other fields as needed

        com.sgiem.ms.security.dto.UserResponse secondUserResponse = userResponses.get(1);
        Assertions.assertEquals(2, secondUserResponse.getIdUser());
        Assertions.assertEquals("Jane", secondUserResponse.getNames());
        Assertions.assertEquals("Smith", secondUserResponse.getSurenames());
        Assertions.assertEquals("JS002", secondUserResponse.getCode());
        Assertions.assertEquals("jane.smith@example.com", secondUserResponse.getEmail());
        Assertions.assertEquals("INACTIVO", secondUserResponse.getState().getValue());
        // Assert other fields as needed
    }

    @Test
    void testListUserState() {
        String stateToSearch = "ACTIVO";

        List<UserCredential> mockUsers = new ArrayList<>();
        UserCredential user1 = new UserCredential();
        user1.setIdUser(1);
        user1.setNames("John");
        user1.setSurenames("Doe");
        user1.setCode("JD001");
        user1.setEmail("john.doe@example.com");
        user1.setState("ACTIVO");
        user1.setRoles(new ArrayList<>());
        mockUsers.add(user1);

        UserCredential user2 = new UserCredential();
        user2.setIdUser(2);
        user2.setNames("Jane");
        user2.setSurenames("Smith");
        user2.setCode("JS002");
        user2.setEmail("jane.smith@example.com");
        user2.setState("ACTIVO");
        user2.setRoles(new ArrayList<>());
        mockUsers.add(user2);

        Mockito.when(authUserService.getAllUsersTitulo(stateToSearch)).thenReturn(mockUsers);

        // Act
        ResponseEntity<List<com.sgiem.ms.security.dto.UserResponse>> responseEntity = authUserController.listUserState(stateToSearch);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<com.sgiem.ms.security.dto.UserResponse> userResponses = responseEntity.getBody();
        Assertions.assertNotNull(userResponses);
        Assertions.assertEquals(2, userResponses.size());

        com.sgiem.ms.security.dto.UserResponse firstUserResponse = userResponses.get(0);
        Assertions.assertEquals(1, firstUserResponse.getIdUser());
        Assertions.assertEquals("John", firstUserResponse.getNames());
        Assertions.assertEquals("Doe", firstUserResponse.getSurenames());
        Assertions.assertEquals("JD001", firstUserResponse.getCode());
        Assertions.assertEquals("john.doe@example.com", firstUserResponse.getEmail());
        Assertions.assertEquals("ACTIVO", firstUserResponse.getState().getValue());
        // Assert other fields as needed
    }

    @Test
    void testGetUserCode() {
        String codeToSearch = "JD001";

        com.sgiem.ms.security.dto.UserResponse user = new com.sgiem.ms.security.dto.UserResponse();
        user.setIdUser(1);
        user.setNames("John");
        user.setSurenames("Doe");
        user.setCode(codeToSearch);
        user.setEmail("john.doe@example.com");
        user.setState(UserResponse.StateEnum.ACTIVO);

        Mockito.when(authUserService.getUserByCode(codeToSearch)).thenReturn(user);

        // Act
        ResponseEntity<com.sgiem.ms.security.dto.UserResponse> responseEntity = authUserController.getUserCode(codeToSearch);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        com.sgiem.ms.security.dto.UserResponse userResponse = responseEntity.getBody();
        Assertions.assertNotNull(userResponse);
        Assertions.assertEquals(1, userResponse.getIdUser());
        Assertions.assertEquals("John", userResponse.getNames());
        Assertions.assertEquals("Doe", userResponse.getSurenames());
        Assertions.assertEquals(codeToSearch, userResponse.getCode());
        Assertions.assertEquals("john.doe@example.com", userResponse.getEmail());
        Assertions.assertEquals("ACTIVO", userResponse.getState().getValue());
        // Assert other fields as needed
    }

    @Test
    void testRegisterUser() {
        com.sgiem.ms.security.dto.UserRequest userRequest = new com.sgiem.ms.security.dto.UserRequest();
        // Configurar userRequest con datos de prueba

        UserCredential userCredential = new UserCredential();
        // Configurar userCredential con datos de prueba

        UserResponse expectedUserResponse = new UserResponse();
        // Configurar expectedUserResponse con datos de prueba

        Mockito.when(authUserService.saveUser(Mockito.any(UserCredential.class))).thenReturn(expectedUserResponse);

        // Act
        ResponseEntity<UserResponse> responseEntity = authUserController.registerUser(userRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserResponse actualUserResponse = responseEntity.getBody();
        Assertions.assertNotNull(actualUserResponse);
        // Verificar que actualUserResponse coincida con expectedUserResponse
    }

    @Test
    void testAssignUser() {
        String titulo = "admin";
        String code = "JD001";

        // Configurar el resultado esperado del servicio authUserService
        UserResponse expectedUserResponse = new UserResponse();
        // Configurar expectedUserResponse con datos de prueba
        Mockito.when(authUserService.assignRolUser(titulo, code)).thenReturn(expectedUserResponse);

        // Act
        ResponseEntity<UserResponse> responseEntity = authUserController.assignUser(titulo, code);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserResponse actualUserResponse = responseEntity.getBody();
        Assertions.assertNotNull(actualUserResponse);
        // Verificar que actualUserResponse coincida con expectedUserResponse
    }

    @Test
    void testResetPassword() {
        String email = "john@example.com";
        com.sgiem.ms.security.dto.PasswordRequest passwordRequest = new com.sgiem.ms.security.dto.PasswordRequest();
        // Configurar passwordRequest con datos de prueba

        // Configurar el resultado esperado del servicio authUserService
        UserResponse expectedUserResponse = new UserResponse();
        // Configurar expectedUserResponse con datos de prueba
        Mockito.when(authUserService.resetPass(email, passwordRequest)).thenReturn(expectedUserResponse);

        // Act
        ResponseEntity<UserResponse> responseEntity = authUserController.resetPassword(email, passwordRequest);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserResponse actualUserResponse = responseEntity.getBody();
        Assertions.assertNotNull(actualUserResponse);
        // Verificar que actualUserResponse coincida con expectedUserResponse
    }

    @Test
    void testChangeState() {
        String code = "JD001";

        // Configurar el resultado esperado del servicio authUserService
        UserResponse expectedUserResponse = new UserResponse();
        // Configurar expectedUserResponse con datos de prueba
        Mockito.when(authUserService.updateStateUser(code)).thenReturn(expectedUserResponse);

        // Act
        ResponseEntity<UserResponse> responseEntity = authUserController.changeState(code);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        UserResponse actualUserResponse = responseEntity.getBody();
        Assertions.assertNotNull(actualUserResponse);
        // Verificar que actualUserResponse coincida con expectedUserResponse
    }
}