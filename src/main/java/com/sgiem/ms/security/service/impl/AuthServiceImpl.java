package com.sgiem.ms.security.service.impl;

import com.google.gson.Gson;
import com.sgiem.ms.security.config.kafka.Producer;
import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.repository.RolCredentialRepositories;
import com.sgiem.ms.security.repository.UserCredentialRepositories;
import com.sgiem.ms.security.service.AuthService;
import com.sgiem.ms.security.utils.commons.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserCredentialRepositories repository;

    @Autowired
    private RolCredentialRepositories rolRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Producer producer;

    @Override
    public String saveUser(UserCredential credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        credential.setCode(Commons.generateCode());

        log.info("User: {}", new Gson().toJson(credential));
//        producer.sendMessage("User: ", new Gson().toJson(credential));

        repository.save(credential);
        return "user added to the system";
    }

    @Override
    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    @Override
    public RolCredential createRol(RolCredential rol) {
        return rolRepository.save(rol);
    }

    @Override
    public List<RolCredential> getAllRoles() {
        return rolRepository.findAll();
    }

    @Override
    public List<UserCredential> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }


}
