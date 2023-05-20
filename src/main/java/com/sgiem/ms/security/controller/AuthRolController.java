package com.sgiem.ms.security.controller;

import com.google.gson.Gson;
import com.sgiem.ms.security.api.v1.RolApi;
import com.sgiem.ms.security.dto.RolResponse;
import com.sgiem.ms.security.service.AuthRolService;
import com.sgiem.ms.security.utils.commons.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth/rol")
public class AuthRolController implements RolApi {

    @Autowired
    private AuthRolService authRolService;

    @Autowired
    private Gson gson;

    @Override
    public ResponseEntity<List<RolResponse>> listRols() {
        List<RolResponse> list = authRolService.findAll()
                .stream()
                .map(rol -> RolResponse.builder()
                        .idRol(rol.getIdRol())
                        .titulo(Commons.validateTituloResponse(rol.getTitulo()))
                        .idUser(rol.getUser().getIdUser())
                        .build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RolResponse>> listRolsTitulo(String titulo) {
        List<RolResponse> list = authRolService.getAllRolsTitulo(titulo)
                .stream()
                .map(rol -> RolResponse.builder()
                        .idRol(rol.getIdRol())
                        .titulo(Commons.validateTituloResponse(rol.getTitulo()))
                        .idUser(rol.getUser().getIdUser())
                        .build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
