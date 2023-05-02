package com.sgiem.ms.security.controller;

import com.sgiem.ms.security.models.dto.AuthRequest;
import com.sgiem.ms.security.models.entity.RolCredential;
import com.sgiem.ms.security.models.entity.UserCredential;
import com.sgiem.ms.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user) {
        return service.saveUser(user);
    }

    @GetMapping("/users")
    public List<UserCredential> getUsers() {
        return service.getAllUsers();
    }

    @PostMapping("/rol")
    public RolCredential addNewrol(@RequestBody RolCredential rol) {
        return service.createRol(rol);
    }

    @GetMapping("/rol")
    public List<RolCredential> getRoles() {
        return service.getAllRoles();
    }

//    @PostMapping("/addRol")
//    public String addRol(@RequestBody RolCredential user) {
//        return service.saveUser(user);
//    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }
}