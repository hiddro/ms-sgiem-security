package com.sgiem.ms.security.controller;

import com.google.gson.Gson;
import com.sgiem.ms.security.api.v1.UserApi;
import com.sgiem.ms.security.dto.RolDetails;
import com.sgiem.ms.security.dto.UserRequest;
import com.sgiem.ms.security.dto.UserResponse;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.service.AuthUserService;
import com.sgiem.ms.security.utils.commons.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth-user")
public class AuthUserController implements UserApi {

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private Gson gson;

    @Override
    public ResponseEntity<List<UserResponse>> listUsers(){
        List<UserResponse> list = authUserService.findAll()
                .stream()
                .map(user -> UserResponse.builder()
                        .idUser(user.getIdUser())
                        .names(user.getNames())
                        .surenames(user.getSurenames())
                        .code(user.getCode())
                        .email(user.getEmail())
                        .roles(Arrays.asList(RolDetails.builder()
                                .idRol(user.getRoles().get(0).getIdRol())
                                .titulo(Commons.validateTitulo(user.getRoles().get(0).getTitulo()))
                                .build()))
                        .build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserResponse> registerUser(UserRequest userRequest){
        UserResponse user = authUserService.saveUser(gson.fromJson(gson.toJson(userRequest), UserCredential.class));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}