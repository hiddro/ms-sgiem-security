package com.sgiem.ms.security.service.impl;

import com.example.Role;
import com.example.UserCredentialDto;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                String temp = credential.getPassword();
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
                credential.setPassword(temp);
                credential.setRoles(Arrays.asList(rol));

                UserCredentialDto userk = new UserCredentialDto();
                Role rolk = new Role();
                userk.setNames(credential.getNames());
                userk.setSurenames(credential.getSurenames());
                userk.setPassword(credential.getPassword());
                userk.setEmail(credential.getEmail());
                userk.setCode(credential.getCode());

                rolk.setTitulo("USER");
                userk.setRoles(rolk);

                producer.sendMessage("User: ", userk);

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
        return userCredentialRepositories.findByCode(code)
                .map(u -> {
                    Commons.validateArrayRoles(titulo, u);

                    RolCredential rol = rolCredentialRepositories.save(RolCredential.builder()
                            .titulo(titulo)
                            .user(u)
                            .build());

                    u.getRoles().add(rol);

                    employeesService.addRolEmployees(titulo, code);

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
                })
                .orElseThrow(() -> new RuntimeException("Valide los parametros enviados!"));
    }

    @Override
    public UserResponse resetPass(String email, PasswordRequest passwordRequest) {
        return userCredentialRepositories.findByEmail(email)
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
                })
                .orElseThrow(() -> new RuntimeException("Valide los parametros enviados!"));
    }

    @Override
    public UserResponse getUserByCode(String code) {
        return userCredentialRepositories.findByCode(code)
                .map(u -> UserResponse.builder()
                        .idUser(u.getIdUser())
                        .names(u.getNames())
                        .surenames(u.getSurenames())
                        .code(u.getCode())
                        .email(u.getEmail())
                        .state(Commons.validateState(u.getState()))
                        .createTime(Commons.validateDate(u.getCreateTime()))
                        .updateTime(Commons.validateDate(u.getUpdateTime()))
                        .roles(Commons.validateRolArray(u))
                        .build())
                .orElseThrow(() -> new RuntimeException("Valide los parametros enviados!"));
    }

    @Override
    public List<UserCredential> getAllUsersTitulo(String state) {
        return userCredentialRepositories.findAll()
                .stream()
                .filter(u -> u.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateStateUser(String code) {
        return userCredentialRepositories.findByCode(code)
                .map(u -> {
                    u.setState(Commons.validateChangeState(u.getState()));
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
                })
                .orElseThrow(() -> new RuntimeException("Valide los parametros enviados!"));
    }
}
