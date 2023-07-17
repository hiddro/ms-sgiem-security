package com.sgiem.ms.security.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeesServiceImplTest {

    @InjectMocks
    private EmployeesServiceImpl employeesService;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    void testAddRolEmployees() {
        // Arrange
        String titulo = "admin";
        String code = "JD001";

        Mockito.when(webClient.put())
                .thenReturn(requestHeadersUriSpec);
        Mockito.when(requestHeadersUriSpec.uri(Mockito.eq("http://localhost:8081/sgiem/employees/assign/{titulo}/{code}"), Mockito.eq(titulo), Mockito.eq(code))).thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.contentType(MediaType.APPLICATION_JSON))
                .thenReturn(requestBodySpec);
        Mockito.when(requestBodySpec.retrieve())
                .thenReturn(responseSpec);
        Mockito.when(responseSpec.toBodilessEntity()).thenReturn(Mono.empty());

        // Act
        employeesService.addRolEmployees(titulo, code);

        // Assert
        // Aquí puedes agregar verificaciones adicionales si esperas algún comportamiento específico del controlador.
    }
}