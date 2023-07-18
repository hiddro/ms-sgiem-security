package com.sgiem.ms.security.utils.commons;

import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommonsTest {

    @InjectMocks
    public Commons commons;

    @Test
    void tesGenerateCode() {
        // Arrange
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;

        // Creamos un SecureRandom con una semilla para tener resultados reproducibles en la prueba
        SecureRandom random = new SecureRandom("seed".getBytes());

        StringBuilder expectedCode = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (random.nextDouble() * (rightLimit - leftLimit + 1));
            while (!((randomLimitedInt >= 48 && randomLimitedInt <= 57) || (randomLimitedInt >= 65 && randomLimitedInt <= 90) || (randomLimitedInt >= 97 && randomLimitedInt <= 122))) {
                randomLimitedInt = leftLimit + (int) (random.nextDouble() * (rightLimit - leftLimit + 1));
            }
            expectedCode.append((char) randomLimitedInt);
        }
        String expected = expectedCode.toString().toUpperCase();

        // Act
        String actual = commons.generateCode();

        // Assert
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(targetStringLength, actual.length());
    }

    @Test
    void validateTituloUser() {
        // Arrange
        String input = "USER";
        com.sgiem.ms.security.dto.RolDetails.TituloEnum expected = com.sgiem.ms.security.dto.RolDetails.TituloEnum.USER;

        // Act
        com.sgiem.ms.security.dto.RolDetails.TituloEnum actual = Commons.validateTitulo(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTituloRehu() {
        // Arrange
        String input = "REHU";
        com.sgiem.ms.security.dto.RolDetails.TituloEnum expected = com.sgiem.ms.security.dto.RolDetails.TituloEnum.REHU;

        // Act
        com.sgiem.ms.security.dto.RolDetails.TituloEnum actual = Commons.validateTitulo(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTituloAdmin() {
        // Arrange
        String input = "ADMIN";
        com.sgiem.ms.security.dto.RolDetails.TituloEnum expected = com.sgiem.ms.security.dto.RolDetails.TituloEnum.ADMIN;

        // Act
        com.sgiem.ms.security.dto.RolDetails.TituloEnum actual = Commons.validateTitulo(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTituloOther() {
        // Arrange
        String input = "OTHER";
        com.sgiem.ms.security.dto.RolDetails.TituloEnum expected = com.sgiem.ms.security.dto.RolDetails.TituloEnum.USER;

        // Act
        com.sgiem.ms.security.dto.RolDetails.TituloEnum actual = Commons.validateTitulo(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTituloResponseUser() {
        // Arrange
        String input = "USER";
        com.sgiem.ms.security.dto.RolResponse.TituloEnum expected = com.sgiem.ms.security.dto.RolResponse.TituloEnum.USER;

        // Act
        com.sgiem.ms.security.dto.RolResponse.TituloEnum actual = Commons.validateTituloResponse(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTituloResponseRehu() {
        // Arrange
        String input = "REHU";
        com.sgiem.ms.security.dto.RolResponse.TituloEnum expected = com.sgiem.ms.security.dto.RolResponse.TituloEnum.REHU;

        // Act
        com.sgiem.ms.security.dto.RolResponse.TituloEnum actual = Commons.validateTituloResponse(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTituloResponseAdmin() {
        // Arrange
        String input = "ADMIN";
        com.sgiem.ms.security.dto.RolResponse.TituloEnum expected = com.sgiem.ms.security.dto.RolResponse.TituloEnum.ADMIN;

        // Act
        com.sgiem.ms.security.dto.RolResponse.TituloEnum actual = Commons.validateTituloResponse(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateTituloResponseOther() {
        // Arrange
        String input = "OTHER";
        com.sgiem.ms.security.dto.RolResponse.TituloEnum expected = com.sgiem.ms.security.dto.RolResponse.TituloEnum.USER;

        // Act
        com.sgiem.ms.security.dto.RolResponse.TituloEnum actual = Commons.validateTituloResponse(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateStateActivo() {
        // Arrange
        String input = "ACTIVO";
        com.sgiem.ms.security.dto.UserResponse.StateEnum expected = com.sgiem.ms.security.dto.UserResponse.StateEnum.ACTIVO;

        // Act
        com.sgiem.ms.security.dto.UserResponse.StateEnum actual = Commons.validateState(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateStateInactivo() {
        // Arrange
        String input = "INACTIVO";
        com.sgiem.ms.security.dto.UserResponse.StateEnum expected = com.sgiem.ms.security.dto.UserResponse.StateEnum.INACTIVO;

        // Act
        com.sgiem.ms.security.dto.UserResponse.StateEnum actual = Commons.validateState(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateStateOther() {
        // Arrange
        String input = "OTHER";
        com.sgiem.ms.security.dto.UserResponse.StateEnum expected = com.sgiem.ms.security.dto.UserResponse.StateEnum.INACTIVO;

        // Act
        com.sgiem.ms.security.dto.UserResponse.StateEnum actual = Commons.validateState(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateChangeStateActivo() {
        // Arrange
        String input = "ACTIVO";
        String expected = "INACTIVO";

        // Act
        String actual = Commons.validateChangeState(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateChangeStateInactivo() {
        // Arrange
        String input = "INACTIVO";
        String expected = "ACTIVO";

        // Act
        String actual = Commons.validateChangeState(input);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateDateOk() {
        // Arrange
        Date inputDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(inputDate);

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expected = LocalDate.parse(formattedDate, inputFormatter);

        // Act
        LocalDate actual = Commons.validateDate(inputDate);

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void validateDateEmpty() {
        // Arrange
        Date date = null;

        // Act
        LocalDate actual = Commons.validateDate(date);

        // Assert
        Assertions.assertNull(actual);
    }

    @Test
    void validateRolArrayNonEmpty() {
        // Arrange
        UserCredential user = new UserCredential();
        List<RolCredential> roles = new ArrayList<>();
        roles.add(new RolCredential(1, "USER", user));
        roles.add(new RolCredential(2, "ADMIN", user));
        user.setRoles(roles);

        // Act
        List<com.sgiem.ms.security.dto.RolDetails> actual = Commons.validateRolArray(user);

        // Assert
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals("USER", actual.get(0).getTitulo().getValue());
        Assertions.assertEquals(1, actual.get(0).getIdRol());
        Assertions.assertEquals(user.getIdUser(), actual.get(0).getIdUser());
        Assertions.assertEquals("ADMIN", actual.get(1).getTitulo().getValue());
        Assertions.assertEquals(2, actual.get(1).getIdRol());
        Assertions.assertEquals(user.getIdUser(), actual.get(1).getIdUser());
    }

    @Test
    void validateRolArrayEmpty() {
        // Arrange
        UserCredential user = new UserCredential();
        user.setRoles(new ArrayList<>());

        // Act
        List<com.sgiem.ms.security.dto.RolDetails> actual = Commons.validateRolArray(user);

        // Assert
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void validateArrayRolesNonDuplicate() {
        // Arrange
        UserCredential user = new UserCredential();
        List<RolCredential> roles = new ArrayList<>();
        roles.add(new RolCredential(1, "USER", user));
        roles.add(new RolCredential(2, "ADMIN", user));
        user.setRoles(roles);

        String titulo = "REHU";

        // Act
        // Assert
        Assertions.assertDoesNotThrow(() -> Commons.validateArrayRoles(titulo, user));
    }

    @Test
    void validateArrayRolesDuplicate() {
        // Arrange
        UserCredential user = new UserCredential();
        List<RolCredential> roles = new ArrayList<>();
        roles.add(new RolCredential(1, "USER", user));
        roles.add(new RolCredential(2, "ADMIN", user));
        user.setRoles(roles);

        String titulo = "USER";

        // Act
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> Commons.validateArrayRoles(titulo, user));

        // Assert
        Assertions.assertEquals("El rol ya esta asignado al usuario!", exception.getMessage());
    }

    @Test
    void convertUserReToCre() {
        // Arrange
        com.sgiem.ms.security.dto.UserRequest userRequest = new com.sgiem.ms.security.dto.UserRequest();
        userRequest.setNames("John");
        userRequest.setSurenames("Doe");
        userRequest.setEmail("john.doe@example.com");
        // Agrega otros atributos necesarios para la prueba

        // Act
        UserCredential result = Commons.convertUserReToCre(userRequest);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(userRequest.getNames(), result.getNames());
        Assertions.assertEquals(userRequest.getSurenames(), result.getSurenames());
        Assertions.assertEquals(userRequest.getEmail(), result.getEmail());
        // Agrega más aserciones para otros atributos si es necesario
    }
}