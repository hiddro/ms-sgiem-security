package com.sgiem.ms.security.service.impl;

import com.google.gson.Gson;
import com.sgiem.ms.security.config.kafka.Producer;
import com.sgiem.ms.security.dto.RolDetails;
import com.sgiem.ms.security.dto.UserResponse;
import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.repository.GenericRepositories;
import com.sgiem.ms.security.repository.RolCredentialRepositories;
import com.sgiem.ms.security.repository.UserCredentialRepositories;
import com.sgiem.ms.security.service.AuthUserService;
import com.sgiem.ms.security.utils.commons.Commons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AuthUserServiceImpl extends CrudServiceImpl<UserCredential, Integer> implements AuthUserService {

    @Autowired
    private UserCredentialRepositories userCredentialRepositories;

    @Autowired
    private RolCredentialRepositories rolCredentialRepositories;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Gson gson;

    @Autowired
    private Producer producer;

    @Override
    protected GenericRepositories<UserCredential, Integer> getRepo() {
        return userCredentialRepositories;
    }

    @Override
    public UserResponse saveUser(UserCredential credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        credential.setCode(Commons.generateCode());

        RolCredential rol = rolCredentialRepositories.findByTitulo("USER").get();

        credential.setRoles(Arrays.asList(rol));

        UserCredential user = userCredentialRepositories.save(credential);

//        log.info("User: {}", new Gson().toJson(credential));
//        producer.sendMessage("User: ", new Gson().toJson(credential));

        return UserResponse.builder()
                .idUser(user.getIdUser())
                .names(user.getNames())
                .surenames(user.getSurenames())
                .code(user.getCode())
                .email(user.getEmail())
                .roles(Arrays.asList(RolDetails.builder()
                        .idRol(user.getRoles().get(0).getIdRol())
                        .titulo(Commons.validateTitulo(user.getRoles().get(0).getTitulo()))
                        .build()))
                .build();
    }
}
