package com.sgiem.ms.security.service.impl;

import com.google.gson.Gson;
import com.sgiem.ms.security.config.kafka.Producer;
import com.sgiem.ms.security.dto.PasswordRequest;
import com.sgiem.ms.security.dto.RolDetails;
import com.sgiem.ms.security.dto.UserResponse;
import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.repository.GenericRepositories;
import com.sgiem.ms.security.repository.RolCredentialRepositories;
import com.sgiem.ms.security.repository.UserCredentialRepositories;
import com.sgiem.ms.security.service.AuthUserService;
import com.sgiem.ms.security.service.EmployeesService;
import com.sgiem.ms.security.utils.commons.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class AuthUserServiceImpl extends CrudServiceImpl<UserCredential, Integer> implements AuthUserService {

    @Autowired
    private UserCredentialRepositories userCredentialRepositories;

    @Autowired
    private RolCredentialRepositories rolCredentialRepositories;

    @Autowired
    private EmployeesService employeesService;

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
    public UserResponse saveUser(UserCredential credential){

        Optional<UserCredential> userBD = Optional.ofNullable(userCredentialRepositories.findByEmail(credential.getEmail()).orElse(new UserCredential()));

        log.info("Validate User!");
        if (userBD.get().getIdUser() != 0){
            throw new RuntimeException("El usuario ya existe!");
        }

        return userBD.map(e -> {
                credential.setPassword(passwordEncoder.encode(credential.getPassword()));
                credential.setCode(Commons.generateCode());
                credential.setState("ACTIVO");

                log.info("Create User Postgresql Azure");
                UserCredential user = userCredentialRepositories.save(credential);

                log.info("Create & Save Rol Postgresql Azure");
                RolCredential rol = rolCredentialRepositories.save(RolCredential.builder()
                        .titulo("USER")
                        .user(user)
                        .build());
                user.setRoles(Arrays.asList(rol));

                log.info("Send Kafka Topic");
                String temp = credential.getPassword();
                credential.setPassword(temp);
                credential.setRoles(Arrays.asList(rol));
    //          producer.sendMessage("User: ", gson.toJson(credential));

                log.info("Response POST");
                return UserResponse.builder()
                        .idUser(user.getIdUser())
                        .names(user.getNames())
                        .surenames(user.getSurenames())
                        .code(user.getCode())
                        .email(user.getEmail())
                        .state(Commons.validateState(user.getState()))
                        .createTime(Commons.validateDate(user.getCreateTime()))
                        .updateTime(Commons.validateDate(user.getUpdateTime()))
                        .roles(Arrays.asList(RolDetails.builder()
                                .idRol(user.getRoles().get(0).getIdRol())
                                .titulo(Commons.validateTitulo(user.getRoles().get(0).getTitulo()))
                                .idUser(user.getRoles().get(0).getUser().getIdUser())
                                .build()))
                        .build();
            }).get();

    }

    @Override
    public UserResponse assignRolUser(String titulo, String code) {
//        Optional<UserCredential> userBD = userCredentialRepositories.findByCode(code);
        return userCredentialRepositories.findByCode(code)
                .map(u -> {
                    Commons.validateArrayRoles(titulo, u);

                    RolCredential rol = rolCredentialRepositories.save(RolCredential.builder()
                            .titulo(titulo)
                            .user(u)
                            .build());

                    u.getRoles().add(rol);

                    return UserResponse.builder()
                            .idUser(u.getIdUser())
                            .names(u.getNames())
                            .surenames(u.getSurenames())
                            .code(u.getCode())
                            .email(u.getEmail())
                            .state(Commons.validateState(u.getState()))
                            .createTime(Commons.validateDate(u.getCreateTime()))
                            .updateTime(Commons.validateDate(u.getUpdateTime()))
                            .roles(Commons.validateRolArray(u))
                            .build();
                }).orElseThrow(() -> new RuntimeException("Valide los parametros enviados!"));
    }

    @Override
    public UserResponse resetPass(String code, PasswordRequest passwordRequest) {
        return userCredentialRepositories.findByCode(code)
                .map(u -> {


                    u.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
                    userCredentialRepositories.save(u);

                    return UserResponse.builder()
                            .idUser(u.getIdUser())
                            .names(u.getNames())
                            .surenames(u.getSurenames())
                            .code(u.getCode())
                            .email(u.getEmail())
                            .state(Commons.validateState(u.getState()))
                            .createTime(Commons.validateDate(u.getCreateTime()))
                            .updateTime(Commons.validateDate(u.getUpdateTime()))
                            .roles(Commons.validateRolArray(u))
                            .build();
                }).orElseThrow(() -> new RuntimeException("Valide los parametros enviados!"));
    }
}
