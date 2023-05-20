package com.sgiem.ms.security.service.impl;

import com.sgiem.ms.security.service.EmployeesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private WebClient webClient;

    @Override
    public void addRolEmployees(String titulo, String code) {
        webClient.put()
                .uri("http://localhost:8081/sgiem/employees/assign/{titulo}/{code}", titulo, code)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}
